package ru.github.fireg45.openweathersdk;

import ru.github.fireg45.openweathersdk.dto.WeatherInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// https://api.openweathermap.org/data/2.5/weather?q={CITY}&appid={API_KEY}

public class OpenWeatherSDK {
    private final String apiKey;
    private final HttpClient httpClient;
    private final OpenWeatherSDKMode mode;
    private final HashMap<String, CachedWeather> weatherCash;

    public OpenWeatherSDK(String apiKey, OpenWeatherSDKMode mode) {
        this.apiKey = apiKey;
        this.mode = mode;
        httpClient = HttpClient.newBuilder().build();
        weatherCash = new HashMap<>();
    }

    public WeatherInfo getWeather(String cityName) {
        if (weatherCash.containsKey(cityName)) {
            CachedWeather cachedWeather = weatherCash.get(cityName);
            if (TimeUnit.MILLISECONDS.toMinutes(cachedWeather.getTimestampInMillis()) < 10) {
                return cachedWeather.getWeatherInfo();
            } else {
                weatherCash.remove(cityName);
                return queryWeather(cityName);
            }
        }
        return queryWeather(cityName);
    }

    private WeatherInfo queryWeather(String cityName) {
        HttpRequest weatherRequest = HttpRequest.newBuilder(
                URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=" + apiKey)
        ).build();
        try {
            HttpResponse<String> response = httpClient.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            WeatherInfo weatherInfo = WeatherInfo.fromJson(response.body());
            weatherCash.put(cityName, new CachedWeather(weatherInfo, System.currentTimeMillis()));
            return weatherInfo;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
