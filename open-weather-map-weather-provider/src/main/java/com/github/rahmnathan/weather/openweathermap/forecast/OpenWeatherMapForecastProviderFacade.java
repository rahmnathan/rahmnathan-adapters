package com.github.rahmnathan.weather.openweathermap.forecast;

import com.github.rahmnathan.weather.forecast.WeatherForecastProvider;
import com.github.rahmnathan.weather.forecast.WeatherSummary;
import org.json.JSONObject;

import java.util.List;

public class OpenWeatherMapForecastProviderFacade implements WeatherForecastProvider {

    @Override
    public List<WeatherSummary> getWeatherForecast(int days, int cityId, String key) {
        OpenWeatherMapForecastProvider forecastProvider = new OpenWeatherMapForecastProvider();

        JSONObject forecastInJson = forecastProvider.getForecastSummaries(cityId, days, key);
        return forecastProvider.jsonToWeatherSummaries(forecastInJson);
    }
}
