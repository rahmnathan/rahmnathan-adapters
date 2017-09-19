package com.github.rahmnathan.weather.openweathermap.current;

import com.github.rahmnathan.http.control.HttpClient;
import com.github.rahmnathan.weather.current.CurrentWeather;
import com.github.rahmnathan.weather.current.CurrentWeatherProvider;
import nr.weatherutils.WindDirection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;

public class OpenWeatherMapCurrentWeatherProvider implements CurrentWeatherProvider {
    private final Logger logger = Logger.getLogger(OpenWeatherMapCurrentWeatherProvider.class.getName());

    @Override
    public CurrentWeather getCurrentWeather(String cityId, String key){
        return assembleCurrentWeather(getContent(cityId, key));
    }

    private CurrentWeather assembleCurrentWeather(JSONObject jsonObject) {
        JSONObject weather = (JSONObject) ((JSONArray) jsonObject.get("weather")).get(0);
        String iconUrl = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";
        JSONObject main = (JSONObject) jsonObject.get("main");
        JSONObject wind = (JSONObject) jsonObject.get("wind");

        return CurrentWeather.Builder.newInstance()
                .highTemp(String.valueOf(main.get("temp_max")))
                .lowTemp(String.valueOf(main.get("temp_min")))
                .temp((String.valueOf(main.get("temp"))).split("\\.")[0])
                .windDirection(WindDirection.degreesToWindDirection((Number) wind.get("deg")))
                .windSpeed(String.valueOf(wind.get("speed")))
                .sky((String) weather.get("description"))
                .icon(HttpClient.getResponseAsBytes(iconUrl))
                .build();
    }

    private JSONObject getContent(String cityId, String key){
        String url = "http://api.openweathermap.org/data/2.5/weather?id=" + cityId + "&units=imperial&appid=" + key;
        return new JSONObject(HttpClient.getResponseAsString(url));
    }
}
