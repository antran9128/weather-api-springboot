package com.skyapi.weatherforecast;

public class GeolocationException extends Throwable {
    public GeolocationException(String message) {
        super(message);
    }

    public GeolocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
