package com.github.rahmnathan.google.commute.provider;

import com.github.rahmnathan.commute.provider.CommuteProvider;
import com.github.rahmnathan.http.control.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.logging.Logger;

public class GoogleCommuteProvider implements CommuteProvider {
    private final Logger logger = Logger.getLogger(GoogleCommuteProvider.class.getName());

    public String getCommuteTime(String startLocation, String endLocation, String key){
        return getTime(getCommuteContent(startLocation, endLocation, key));
    }

    private JSONObject getCommuteContent(String startLocation, String endLocation, String key){
        String uri = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLocation +
                "&destination=" + endLocation + "&key=" + key;

        String response = HttpClient.getResponseAsString(uri);
        return new JSONObject(response);
    }

    private String getTime(JSONObject jsonObject){
        JSONObject firstRoute = ((JSONArray) jsonObject.get("routes")).getJSONObject(0);
        JSONObject legs = ((JSONArray) firstRoute.get("legs")).getJSONObject(0);
        JSONObject duration = (JSONObject) legs.get("duration");
        return duration.getString("text");
    }
}
