package com.github.rahmnathan.weather.openweathermap.forecast;

import com.github.rahmnathan.http.control.HttpClient;
import com.github.rahmnathan.weather.forecast.WeatherSummary;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OpenWeatherMapForecastProvider {
    private final Logger logger = Logger.getLogger(OpenWeatherMapForecastProvider.class.getName());

    List<WeatherSummary> getForecastSummaries(int cityId, int days, String key) {
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?id=" + cityId + "&cnt=" + days + "&units=imperial&appid=" + key;

        JSONObject weather = new JSONObject(HttpClient.getResponseAsString(url));
        return jsonToWeatherSummaries(weather);
    }

    List<WeatherSummary> jsonToWeatherSummaries(JSONObject jsonObject){
        if(!jsonObject.has("list"))
            return new ArrayList<>();

        JSONArray weatherList = jsonObject.getJSONArray("list");
        List<WeatherSummary> summaryList = new ArrayList<>();

        for(Object jsonDay : weatherList) {
            JSONObject object = (JSONObject) jsonDay;

            JSONObject temp = object.getJSONObject("temp");

            WeatherSummary.Builder builder = WeatherSummary.Builder.newInstance();
            builder.setHighTemp(Double.valueOf(temp.getDouble("max")).intValue())
                .setLowTemp(Double.valueOf(temp.get("min").toString()).intValue())
                .setHumidity(object.get("humidity").toString())
                .setWindSpeed(Double.valueOf(object.getDouble("speed")).intValue())
                .setDateTime(object.getLong("dt"));

            JSONObject weather = (JSONObject) object.getJSONArray("weather").get(0);
            String iconUrl = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";

            builder.setSky(weather.getString("main"))
                .setIcon( HttpClient.getResponseAsBytes(iconUrl));

            summaryList.add(builder.build());
        }

        return summaryList;
    }
}
