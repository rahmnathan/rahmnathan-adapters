package com.github.rahmnathan.weather.current;

public interface CurrentWeatherProvider {

    CurrentWeather getCurrentWeather(String city, String key);
}