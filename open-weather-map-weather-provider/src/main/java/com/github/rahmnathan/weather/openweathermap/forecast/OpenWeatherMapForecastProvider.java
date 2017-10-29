package com.github.rahmnathan.weather.openweathermap.forecast;

import com.github.rahmnathan.http.control.HttpClient;
import com.github.rahmnathan.http.data.HttpRequestMethod;
import com.github.rahmnathan.weather.forecast.WeatherForecastProvider;
import com.github.rahmnathan.weather.forecast.WeatherSummary;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OpenWeatherMapForecastProvider implements WeatherForecastProvider {
    private final Logger logger = Logger.getLogger(OpenWeatherMapForecastProvider.class.getName());
    private final String apiKey;

    public OpenWeatherMapForecastProvider(String apiKey){
        this.apiKey = apiKey;
    }

    @Override
    public List<WeatherSummary> getWeatherForecast(int numberOfDays, int zipCode) {
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?zip=" + zipCode + "&cnt="
                + numberOfDays + "&units=imperial&appid=" + apiKey;

        JSONObject weather = new JSONObject(HttpClient.getResponseAsString(url, HttpRequestMethod.GET, null, null));
        return jsonToWeatherSummaries(weather);
    }

    private List<WeatherSummary> jsonToWeatherSummaries(JSONObject jsonObject){
        if(!jsonObject.has("list"))
            return new ArrayList<>();

        JSONArray weatherList = jsonObject.getJSONArray("list");
        List<WeatherSummary> summaryList = new ArrayList<>();

        weatherList.iterator().forEachRemaining( day -> {
            JSONObject dayJson = (JSONObject) day;
            JSONObject temp = dayJson.getJSONObject("temp");

            WeatherSummary.Builder builder = WeatherSummary.Builder.newInstance()
                    .setHighTemp(Double.valueOf(temp.getDouble("max")).intValue())
                    .setLowTemp(Double.valueOf(temp.get("min").toString()).intValue())
                    .setHumidity(dayJson.get("humidity").toString())
                    .setWindSpeed(Double.valueOf(dayJson.getDouble("speed")).intValue())
                    .setDateTime(dayJson.getLong("dt"));

            JSONObject weather = dayJson.getJSONArray("weather").getJSONObject(0);
            String iconUrl = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";

            builder.setSky(weather.getString("main"))
                    .setIcon(HttpClient.getResponseAsBytes(iconUrl));

            summaryList.add(builder.build());
        });

        return summaryList;
    }
}
