package com.github.rahmnathan.localmovies.omdb.info.provider;

import org.json.JSONObject;

class TestUtils {

    static JSONObject getJsonMovieInfo(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imdbRating", "10");
        jsonObject.put("Metascore", "11");
        jsonObject.put("Year", "1900");
        jsonObject.put("Genre", "Action");
        jsonObject.put("Poster", "Test");

        return jsonObject;
    }
}
