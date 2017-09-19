package com.github.rahmnathan.weather.current;

public interface CurrentWeatherProvider {

    CurrentWeather getCurrentWeather(int zipCode);
}