package ru.github.fireg45;

import ru.github.fireg45.openweathersdk.OpenWeatherSDK;
import ru.github.fireg45.openweathersdk.OpenWeatherSDKMode;

public class App {
    public static void main(String[] args) throws InterruptedException {
        String API_KEY1 = "99772a99f28f083f12c0002190d6c9bf";
        String API_KEY2 = "013d95767808ebb2c93ece707c178b70";

        OpenWeatherSDK openWeatherSDK1 = OpenWeatherSDK.factory(API_KEY1, OpenWeatherSDKMode.POLLING);
        OpenWeatherSDK openWeatherSDK2 = OpenWeatherSDK.factory(API_KEY2, OpenWeatherSDKMode.POLLING);

        String[] cities = new String[]{
                "Казань",
                "Москва",
                "Сызрань",
                "Нижнекамск",
                "Сыктывкар",
                "Абакан",
                "Лондон",
                "Нигер",
                "Самара",
                "Биробиджан"
        };

        int i = 0;

        while (true) {
            System.out.println(openWeatherSDK1.getWeatherAsJsonObject(cities[(++i) % cities.length]).getJSONObject("weather").getString("main"));
            System.out.println(openWeatherSDK2.getWeatherAsJsonObject(cities[(++i) % cities.length]).getJSONObject("weather").getString("main"));
        }
    }
}
