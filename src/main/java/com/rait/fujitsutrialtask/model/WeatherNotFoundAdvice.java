package com.rait.fujitsutrialtask.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handler for the weather not found for certain ID exception.
 */
@ControllerAdvice
public class WeatherNotFoundAdvice {

    //Return HTTP status, error message from the exception class.
    @ResponseBody
    @ExceptionHandler(WeatherNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String weatherNotFoundHandler(WeatherNotFoundException ex) {
        return ex.getMessage();
    }
}