package ru.github.fireg45;

import ru.github.fireg45.openweathersdk.OpenWeatherSDK;
import ru.github.fireg45.openweathersdk.OpenWeatherSDKMode;

public class App {
    public static void main( String[] args ) {
        String API_KEY = "99772a99f28f083f12c0002190d6c9bf";

        OpenWeatherSDK openWeatherSDK = new OpenWeatherSDK(API_KEY, OpenWeatherSDKMode.POLLING);
        System.out.println(openWeatherSDK.getWeather("Kazan").getWeather());
    }
}
