package com.github.rahmnathan.localmovies.omdb.info.provider;

import com.github.rahmnathan.http.control.HttpClient;
import com.google.common.io.ByteStreams;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class OmdbRawDataProvider {
    private final Logger logger = Logger.getLogger(OmdbRawDataProvider.class.getName());
    private final String apiKey;

    public OmdbRawDataProvider(String apiKey){
        this.apiKey = apiKey;
    }

    JSONObject loadMovieInfo(String title) {
        String response = "";
        try {
            String url = "http://www.omdbapi.com/?t=" + URLEncoder.encode(title, StandardCharsets.UTF_8.name()) + "&apikey=" + apiKey;
            logger.info("Loading MovieInfo from OMDB - " + url);

            response = HttpClient.getResponseAsString(url);
        } catch (UnsupportedEncodingException e){
            logger.severe(e.toString());
        }

        return new JSONObject(response);
    }

    byte[] loadMoviePoster(URL imageURL) {
        try(InputStream is = imageURL.openConnection().getInputStream()){
            return ByteStreams.toByteArray(is);
        } catch (IOException e){
            logger.fine(e.toString());
            return new byte[0];
        }
    }
}
