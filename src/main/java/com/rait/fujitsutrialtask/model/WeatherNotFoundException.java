package com.rait.fujitsutrialtask.model;

/**
 * Error for when weather data is not found for a given ID.
 */
public class WeatherNotFoundException extends RuntimeException {

    //Error message.
    WeatherNotFoundException(Long id) {
        super("Weather data does not exist for ID " + id);
    }
}
