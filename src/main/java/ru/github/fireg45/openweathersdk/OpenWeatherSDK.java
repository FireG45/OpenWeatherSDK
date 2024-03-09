/**
 * The OpenWeatherSDK class provides functionality to retrieve weather information from the OpenWeatherMap API.
 */
package ru.github.fireg45.openweathersdk;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import ru.github.fireg45.openweathersdk.dto.WeatherInfo;
import ru.github.fireg45.openweathersdk.exception.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * The OpenWeatherSDK class acts as interface for interacting with the OpenWeatherMap API.
 */
public class OpenWeatherSDK {
    private final String apiKey;
    @Setter
    private HttpClient httpClient;
    @Setter
    @Getter
    private OpenWeatherSDKMode mode;
    @Setter
    @Getter
    private  boolean polling;
    private final HashMap<String, CachedWeather> weatherCash;
    private final int CACHE_MAX_SIZE = 10;
    private final long WEATHER_ACTUALITY_TIME = TimeUnit.MINUTES.toMillis(10);
    private static final HashMap<String, OpenWeatherSDK> objectPool = new HashMap<>();

    /**
     * Factory method to create or get (from pool) an instance of OpenWeatherSDK.
     *
     * @param apiKey The API key for accessing the OpenWeatherMap API.
     * @param mode   The mode of operation for the SDK. (Polling or On Demand)
     * @return An instance of OpenWeatherSDK.
     */
    public static OpenWeatherSDK factory(String apiKey, OpenWeatherSDKMode mode) {
        OpenWeatherSDK weatherSDK;
        if (objectPool.containsKey(apiKey)) {
            weatherSDK = objectPool.get(apiKey);
            weatherSDK.setMode(mode);
            weatherSDK.setPolling(mode == OpenWeatherSDKMode.POLLING);
        } else {
            weatherSDK = new OpenWeatherSDK(apiKey, mode);
            objectPool.put(apiKey, weatherSDK);
        }
        return weatherSDK;
    }

    private OpenWeatherSDK(String apiKey, OpenWeatherSDKMode mode) {
        this.apiKey = apiKey;
        this.mode = mode;
        httpClient = HttpClient.newBuilder().build();
        weatherCash = new HashMap<>();
        polling = mode == OpenWeatherSDKMode.POLLING;
    }

    /**
     * Releases the SDK object and removes it from the object pool.
     */
    public void releaseSDKObject() {
        objectPool.remove(apiKey);
        System.gc();
    }

    /**
     * Retrieves weather information for a specific city as a WeatherInfo object.
     *
     * @param cityName The name of the city to retrieve weather info for.
     * @return WeatherInfo object containing weather information.
     */
    public WeatherInfo getWeatherInfo(String cityName) {
        return !polling ? weatherRequestOnDemand(cityName) : weatherRequestPolling(cityName);
    }

    /**
     * Retrieves weather information for a specific city as a JSONObject.
     *
     * @param cityName The name of the city to retrieve weather info for.
     * @return Weather information as a JSONObject.
     */
    public JSONObject getWeatherAsJsonObject(String cityName) {
        return getWeatherInfo(cityName).toJSON();
    }

    /**
     * Retrieves weather information for a specific city as a JSON string.
     *
     * @param cityName The name of the city to retrieve weather info for.
     * @return Weather information as a JSON string.
     */
    public String getWeatherAsJsonString(String cityName) {
        return getWeatherInfo(cityName).toJSON().toString();
    }

    /**
     * Check if the cached weather information has expired.
     *
     * @param cachedWeather The cached weather information.
     * @return True if the weather information has expired, false otherwise.
     */
    private boolean weatherIsExpired(CachedWeather cachedWeather) {
        return System.currentTimeMillis() - cachedWeather.getTimestampInMillis() > WEATHER_ACTUALITY_TIME;
    }

    /**
     * Construct an HTTP request for the given city name.
     *
     * @param cityName The name of the city for the weather request.
     * @return HttpRequest object for the city weather request.
     */
    private HttpRequest getRequest(String cityName) {
        return HttpRequest.newBuilder(
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey)
        ).build();
    }

    /**
     * Perform a weather request in polling mode for the given city.
     *
     * @param cityName The name of the city for the weather request.
     * @return WeatherInfo object containing weather information.
     */
    private WeatherInfo weatherRequestPolling(String cityName) {
        boolean newCity = false;
        if (!weatherCash.containsKey(cityName)) {
            cacheWeatherInfo(cityName, handleRequest(getRequest(cityName), cityName));
            newCity = true;
        }
        for (String key : weatherCash.keySet()) {
            if (newCity && Objects.equals(key, cityName)) continue;
            CachedWeather cachedWeather = weatherCash.get(key);
            if (weatherIsExpired(cachedWeather)) {
                HttpRequest weatherRequest = getRequest(key);
                cacheWeatherInfo(key, handleRequest(weatherRequest, key));
            }
        }
        return getCachedWeather(cityName).getWeatherInfo();
    }

    /**
     * Perform a weather request on-demand for the city.
     *
     * @param city The name of the city for the weather request.
     * @return WeatherInfo object containing weather information.
     */
    private WeatherInfo weatherRequestOnDemand(String city) {
        if (weatherCash.containsKey(city)) {
            CachedWeather cachedWeather = getCachedWeather(city);
            if (System.currentTimeMillis() - cachedWeather.getTimestampInMillis() < WEATHER_ACTUALITY_TIME) {
                return cachedWeather.getWeatherInfo();
            } else {
                weatherCash.remove(city);
                return queryWeather(city);
            }
        }
        return queryWeather(city);
    }

    /**
     * Query the weather information from the API for the given city.
     *
     * @param cityName The name of the city for the weather query.
     * @return WeatherInfo object containing weather information.
     */
    private WeatherInfo queryWeather(String cityName) {
        HttpRequest weatherRequest = HttpRequest.newBuilder(
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey)
        ).build();
        return handleRequest(weatherRequest, cityName);
    }

    /**
     * Handle the HTTP request for weather information retrieval.
     *
     * @param weatherRequest The HttpRequest object for the weather request.
     * @param cityName        The name of the city for the weather request.
     * @return WeatherInfo object containing weather information.
     */
    private WeatherInfo handleRequest(HttpRequest weatherRequest, String cityName) {
        try {
            HttpResponse<String> response = httpClient.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                WeatherInfo weatherInfo = WeatherInfo.fromJson(response.body());
                cacheWeatherInfo(cityName, weatherInfo);
                return weatherInfo;
            } else {
                JSONObject jsonObject = new JSONObject(response.body());
                String message = jsonObject.getString("message");
                switch (response.statusCode()) {
                    case 400 -> throw new OpenWeatherSDKBadRequestException(message);
                    case 401 -> throw new OpenWeatherSDKUnauthorizedException(message);
                    case 404 -> throw new OpenWeatherSDKNotFoundException(message);
                    case 429 -> throw new OpenWeatherSDKTooManyRequestsException(message);
                    default -> throw new OpenWeatherSDKServerException(message);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the cached weather information for the given city.
     *
     * @param cityName The name of the city for cached weather retrieval.
     * @return CachedWeather object containing cached weather information.
     */
    private CachedWeather getCachedWeather(String cityName) {
        return weatherCash.get(cityName);
    }

    /**
     * Cache the weather information for the city.
     *
     * @param city        The name of the city for caching weather information.
     * @param weatherInfo The WeatherInfo object to cache.
     */
    private void cacheWeatherInfo(String city, WeatherInfo weatherInfo) {
        if (weatherCash.size() >= CACHE_MAX_SIZE) {
            String maxKey = "";
            long maxTime = 0;
            for (String key : weatherCash.keySet()) {
                long time = weatherCash.get(key).getTimestampInMillis();
                if ((System.currentTimeMillis() - time) > maxTime) {
                    maxTime = time;
                    maxKey = key;
                }
            }
            weatherCash.remove(maxKey);
        }
        weatherCash.put(city, new CachedWeather(weatherInfo));
    }
}