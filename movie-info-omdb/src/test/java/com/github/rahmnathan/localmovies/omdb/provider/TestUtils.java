package com.github.rahmnathan.localmovies.omdb.provider;

import org.json.JSONObject;

public class TestUtils {

    public static JSONObject getJsonMovieInfo(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imdbRating", "10");
        jsonObject.put("Metascore", "11");
        jsonObject.put("Year", "1900");
        jsonObject.put("Genre", "Action");
        jsonObject.put("Poster", "Test");

        return jsonObject;
    }
}
