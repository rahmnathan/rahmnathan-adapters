package com.github.rahmnathan.weather.openweathermap.current;

import com.github.rahmnathan.http.control.HttpClient;
import com.github.rahmnathan.http.data.HttpRequestMethod;
import com.github.rahmnathan.weather.current.CurrentWeather;
import com.github.rahmnathan.weather.current.CurrentWeatherProvider;
import org.json.JSONObject;

public class OpenWeatherMapCurrentWeatherProvider implements CurrentWeatherProvider {
    private final String apiKey;

    public OpenWeatherMapCurrentWeatherProvider(String apiKey){
        this.apiKey = apiKey;
    }

    @Override
    public CurrentWeather getCurrentWeather(int zipCode){
        JSONObject content = getContent(zipCode);
        return assembleCurrentWeather(content);
    }

    private CurrentWeather assembleCurrentWeather(JSONObject jsonObject) {
        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        JSONObject main = jsonObject.getJSONObject("main");
        JSONObject wind = jsonObject.getJSONObject("wind");
        String iconUrl = "http://openweathermap.org/img/w/" + weather.getString("icon") + ".png";

        return CurrentWeather.Builder.newInstance()
                .highTemp(main.getString("temp_max"))
                .lowTemp(main.getString("temp_min"))
                .temp((main.getString("temp")).split("\\.")[0])
                .windDirection(wind.getString("deg"))
                .windSpeed(wind.getString("speed"))
                .sky( weather.getString("description"))
                .icon(HttpClient.getResponseAsBytes(iconUrl))
                .build();
    }

    private JSONObject getContent(int zipCode){
        String url = "http://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + "&units=imperial&appid=" + apiKey;
        return new JSONObject(HttpClient.getResponseAsString(url, HttpRequestMethod.GET, null, null));
    }
}
