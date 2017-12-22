package com.github.rahmnathan.google.commute.provider;

import com.github.rahmnathan.http.control.HttpClient;
import com.github.rahmnathan.http.data.HttpRequestMethod;
import org.json.JSONObject;

public class GoogleCommuteProvider {
    private final String apiKey;

    public GoogleCommuteProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getCommuteTime(String startLocation, String endLocation){
        JSONObject jsonCommute = getCommuteContent(startLocation, endLocation);
        return getTime(jsonCommute);
    }

    private JSONObject getCommuteContent(String startLocation, String endLocation){
        String uri = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLocation +
                "&destination=" + endLocation + "&key=" + apiKey;

        String response = HttpClient.getResponseAsString(uri, HttpRequestMethod.GET, null, null);
        return new JSONObject(response);
    }

    private String getTime(JSONObject jsonObject){
        JSONObject firstRoute = jsonObject.getJSONArray("routes").getJSONObject(0);
        JSONObject legs = firstRoute.getJSONArray("legs").getJSONObject(0);
        JSONObject duration = legs.getJSONObject("duration");
        return duration.getString("text");
    }
}
