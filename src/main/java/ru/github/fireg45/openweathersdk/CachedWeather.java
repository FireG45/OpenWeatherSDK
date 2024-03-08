package ru.github.fireg45.openweathersdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.github.fireg45.openweathersdk.dto.WeatherInfo;


@Data
@AllArgsConstructor
public class CachedWeather {
    private WeatherInfo weatherInfo;
    private long timestampInMillis;
}
