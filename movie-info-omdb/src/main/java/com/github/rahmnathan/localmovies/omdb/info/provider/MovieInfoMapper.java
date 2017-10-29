package com.github.rahmnathan.localmovies.omdb.info.provider;

import com.github.rahmnathan.movie.info.data.MovieInfo;
import org.json.JSONObject;

import java.util.Base64;

class MovieInfoMapper {

    MovieInfo jsonToMovieInfo(JSONObject jsonObject, String title){
        MovieInfo.Builder builder = mapMovieInfo(jsonObject, title);

        return builder.build();
    }

    MovieInfo jsonToMovieInfo(JSONObject jsonObject, byte[] poster, String title){
        MovieInfo.Builder movieInfoBuilder = mapMovieInfo(jsonObject, title);
        mapPoster(movieInfoBuilder, poster);

        return movieInfoBuilder.build();
    }

    private MovieInfo.Builder mapMovieInfo(JSONObject jsonObject, String title){
        MovieInfo.Builder movieInfoBuilder = MovieInfo.Builder.newInstance();
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

    private MovieInfo.Builder mapPoster(MovieInfo.Builder movieInfoBuilder, byte[] poster){
        try {
            movieInfoBuilder.setImage(Base64.getEncoder().encodeToString(poster));
        } catch (Exception e) {
            movieInfoBuilder.setImage(null);
        }

        return movieInfoBuilder;
    }
}
