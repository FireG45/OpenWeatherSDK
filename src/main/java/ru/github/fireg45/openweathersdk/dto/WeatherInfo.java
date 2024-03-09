package ru.github.fireg45.openweathersdk.dto;

import lombok.Data;
import org.json.JSONObject;

@Data
public class WeatherInfo {
    private Weather weather;
    private Temperature temperature;
    private int visibility;
    private Wind wind;
    private long datetime;
    private Sys sys;
    private int timezone;
    private String name;

    public static WeatherInfo fromJson(String body) {
        JSONObject jsonObject = new JSONObject(body);
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setWeather(new Weather(
                jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"),
                jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"))
        );
        weatherInfo.setTemperature(new Temperature(
                jsonObject.getJSONObject("main").getDouble("temp"),
                jsonObject.getJSONObject("main").getDouble("feels_like")
        ));
        weatherInfo.setVisibility(jsonObject.getInt("visibility"));
        weatherInfo.setWind(new Wind(
                jsonObject.getJSONObject("wind").getInt("speed")
        ));
        weatherInfo.setDatetime(jsonObject.getLong("dt"));
        weatherInfo.setTimezone(jsonObject.getInt("timezone"));
        weatherInfo.setSys(new Sys(
                jsonObject.getJSONObject("sys").getLong("sunrise"),
                jsonObject.getJSONObject("sys").getLong("sunset")
        ));
        weatherInfo.setName(jsonObject.getString("name"));

        return weatherInfo;
    }

    public JSONObject toJSON() {
        return new JSONObject(this);
    }
}
