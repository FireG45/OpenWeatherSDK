package io.github.fireg45.openweathersdk;

import lombok.Data;
import io.github.fireg45.openweathersdk.dto.WeatherInfo;


@Data
public class CachedWeather {
    private WeatherInfo weatherInfo;
    private long timestampInMillis;

    public CachedWeather(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
        timestampInMillis = System.currentTimeMillis();
    }
}
