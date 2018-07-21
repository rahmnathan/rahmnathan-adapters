package com.github.rahmnathan.omdb.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.rahmnathan.omdb.data.Movie;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.Base64;

import static com.github.rahmnathan.omdb.config.OmdbCamelRoutes.*;

public class MovieBuilder implements Processor {
    private static final String IMDB_RATING_FIELD = "imdbRating";
    private static final String META_SCORE_FIELD = "Metascore";
    private static final String ACTORS_FIELD = "Actors";
    private static final String GENRE_FIELD = "Genre";
    private static final String YEAR_FIELD = "Year";
    private static final String PLOT_FIELD = "Plot";

    @Override
    public void process(Exchange exchange) {
        Message inMessage = exchange.getIn();

        JsonNode movieJson = exchange.getProperty(OMDB_DATA_PROPERTY, JsonNode.class);
        Movie.Builder builder = mapMovieInfo(movieJson, exchange.getProperty(MOVIE_TITLE_PROPERTY, String.class));
        mapMoviePoster(inMessage, builder);

        exchange.getOut().setBody(builder.build());
    }

    private Movie.Builder mapMovieInfo(JsonNode movieJson, String title) {
        Movie.Builder movieInfoBuilder = Movie.Builder.newInstance();
        movieInfoBuilder.setTitle(title);

        if (movieJson.has(IMDB_RATING_FIELD))
            movieInfoBuilder.setIMDBRating(movieJson.get(IMDB_RATING_FIELD).asText());
        if (movieJson.has(META_SCORE_FIELD))
            movieInfoBuilder.setMetaRating(movieJson.get(META_SCORE_FIELD).asText());
        if (movieJson.has(YEAR_FIELD))
            movieInfoBuilder.setReleaseYear(movieJson.get(YEAR_FIELD).asText());
        if (movieJson.has(GENRE_FIELD))
            movieInfoBuilder.setGenre(movieJson.get(GENRE_FIELD).asText());
        if (movieJson.has(PLOT_FIELD))
            movieInfoBuilder.setPlot(movieJson.get(PLOT_FIELD).asText());
        if (movieJson.has(ACTORS_FIELD))
            movieInfoBuilder.setActors(movieJson.get(ACTORS_FIELD).asText());

        return movieInfoBuilder;
    }

    private void mapMoviePoster(Message inMessage, Movie.Builder builder) {
        byte[] poster = inMessage.getBody(byte[].class);
        if (poster != null) {
            builder.setImage(Base64.getEncoder().encodeToString(poster));
        }
    }
}
