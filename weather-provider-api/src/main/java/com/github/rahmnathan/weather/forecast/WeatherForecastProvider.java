package com.github.rahmnathan.weather.forecast;

import java.util.List;

public interface WeatherForecastProvider {

    List<WeatherSummary> getWeatherForecast(int numberOfDays, int zipCode);
}
