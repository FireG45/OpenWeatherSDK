package ru.github.fireg45.openweathersdk;

import jdk.jshell.spi.ExecutionControl;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.util.HashMap;

// https://api.openweathermap.org/data/2.5/weather?q={CITY}&appid={API_KEY}

public class OpenWeatherSDK {
    private final String apiKey;
    private final HttpClient httpClient;
    private final OpenWeatherSDKMode mode;
    private HashMap<String, JSONObject> weatherCash;

    public OpenWeatherSDK(String apiKey, OpenWeatherSDKMode mode) {
        this.apiKey = apiKey;
        this.mode = mode;
        httpClient = HttpClient.newBuilder().build();
        weatherCash = new HashMap<>();
    }

    public String getWeather(String cityName) {
        return null;
    }

}
