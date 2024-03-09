package ru.github.fireg45.openweathersdk.exception;

public class OpenWeatherSDKTooManyRequestsException extends RuntimeException {
    public OpenWeatherSDKTooManyRequestsException(String message) {
        super(message);
    }
}
