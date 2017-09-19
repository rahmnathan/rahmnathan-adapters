package com.github.rahmnathan.google.commute.provider;

import com.github.rahmnathan.commute.provider.CommuteProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class GoogleCommuteProvider implements CommuteProvider {
    private final Logger logger = Logger.getLogger(GoogleCommuteProvider.class.getName());

    public String getCommuteTime(String startLocation, String endLocation, String key){
        return getTime(getCommuteContent(startLocation, endLocation, key));
    }

    private JSONObject getCommuteContent(String startLocation, String endLocation, String key){
        String uri = "https://maps.googleapis.com/maps/api/directions/json?origin=" + startLocation +
                "&destination=" + endLocation + "&key=" + key;

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(uri).openConnection();
        } catch (IOException e){
            logger.severe(e.toString());
        }

        if(connection != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                reader.lines().forEachOrdered(response::append);

                return new JSONObject(response);
            } catch (IOException e) {
                logger.severe(e.toString());
            } finally {
                connection.disconnect();
            }
        }

        return new JSONObject();
    }

    private String getTime(JSONObject jsonObject){
        JSONObject firstRoute = ((JSONArray) jsonObject.get("routes")).getJSONObject(0);
        JSONObject legs = ((JSONArray) firstRoute.get("legs")).getJSONObject(0);
        JSONObject duration = (JSONObject) legs.get("duration");
        return duration.getString("text");
    }
}
