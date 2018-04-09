package com.github.rahmnathan.localmovies.omdb.provider.control;

import com.github.rahmnathan.movie.data.Movie;
import org.json.JSONObject;

import java.util.Base64;

public class MovieInfoMapper {

    private MovieInfoMapper(){

    }

    public static Movie jsonToMovieInfo(JSONObject jsonObject, String title){
        Movie.Builder builder = mapMovieInfo(jsonObject, title);

        return builder.build();
    }

    public static Movie jsonToMovieInfo(JSONObject jsonObject, String title, byte[] poster){
        Movie.Builder movieInfoBuilder = mapMovieInfo(jsonObject, title);
        mapPoster(movieInfoBuilder, poster);

        return movieInfoBuilder.build();
    }

    public static Movie.Builder mapMovieInfo(JSONObject jsonObject, String title){
        Movie.Builder movieInfoBuilder = Movie.Builder.newInstance();
        movieInfoBuilder.setTitle(title);

        if(jsonObject.has("imdbRating"))
            movieInfoBuilder.setIMDBRating(jsonObject.getString("imdbRating"));
        if(jsonObject.has("Metascore"))
            movieInfoBuilder.setMetaRating(jsonObject.getString("Metascore"));
        if(jsonObject.has("Year"))
            movieInfoBuilder.setReleaseYear(jsonObject.getString("Year"));
        if(jsonObject.has("Genre"))
            movieInfoBuilder.setGenre(jsonObject.getString("Genre"));

        return movieInfoBuilder;
    }

    private static void mapPoster(Movie.Builder movieInfoBuilder, byte[] poster){
        try {
            movieInfoBuilder.setImage(Base64.getEncoder().encodeToString(poster));
        } catch (Exception e) {
            movieInfoBuilder.setImage(null);
        }
    }
}
