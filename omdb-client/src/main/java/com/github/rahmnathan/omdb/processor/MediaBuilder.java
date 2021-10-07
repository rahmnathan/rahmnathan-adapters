package com.github.rahmnathan.omdb.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.rahmnathan.omdb.data.Media;
import com.github.rahmnathan.omdb.data.MediaType;
import com.github.rahmnathan.omdb.exception.MediaNotFoundException;
import com.github.rahmnathan.omdb.exception.MediaProviderException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.Base64;

import static com.github.rahmnathan.omdb.config.OmdbCamelRoutes.*;

public record MediaBuilder(MediaType mediaType) implements Processor {
    private static final String IMDB_RATING_FIELD = "imdbRating";
    private static final String META_SCORE_FIELD = "Metascore";
    private static final String ACTORS_FIELD = "Actors";
    private static final String GENRE_FIELD = "Genre";
    private static final String YEAR_FIELD = "Year";
    private static final String PLOT_FIELD = "Plot";

    @Override
    public void process(Exchange exchange) throws MediaProviderException {
        Message inMessage = exchange.getIn();

        JsonNode mediaJson = exchange.getProperty(OMDB_DATA_PROPERTY, JsonNode.class);

        Media.MediaBuilder mediaBuilder = Media.builder();
        mediaBuilder.title(exchange.getProperty(MEDIA_TITLE_PROPERTY, String.class));
        mediaBuilder.mediaType(mediaType);

        if (mediaJson == null || mediaJson.get("Response").asText().equalsIgnoreCase("False")) {
            throw new MediaNotFoundException("Media not found.");
        }

        mapMovieInfo(mediaJson, mediaBuilder);
        mapMoviePoster(inMessage, mediaBuilder);

        if (mediaType == MediaType.EPISODE || mediaType == MediaType.SEASON) {
            Integer number = exchange.getProperty(NUMBER_PROPERTY, Integer.class);
            mediaBuilder.number(number);
            if (mediaJson.has("Title")) {
                mediaBuilder.title(mediaJson.get("Title").asText());
            } else if (MediaType.EPISODE == mediaType) {
                mediaBuilder.title("Episode " + number);
            } else {
                mediaBuilder.title("Season " + number);
            }
        }

        exchange.getOut().setBody(mediaBuilder.build());
    }

    private void mapMovieInfo(JsonNode mediaJson, Media.MediaBuilder builder) {
        if (mediaJson.has(IMDB_RATING_FIELD))
            builder.imdbRating(mediaJson.get(IMDB_RATING_FIELD).asText());
        if (mediaJson.has(META_SCORE_FIELD))
            builder.metaRating(mediaJson.get(META_SCORE_FIELD).asText());
        if (mediaJson.has(YEAR_FIELD))
            builder.releaseYear(mediaJson.get(YEAR_FIELD).asText());
        if (mediaJson.has(GENRE_FIELD))
            builder.genre(mediaJson.get(GENRE_FIELD).asText());
        if (mediaJson.has(PLOT_FIELD))
            builder.plot(mediaJson.get(PLOT_FIELD).asText());
        if (mediaJson.has(ACTORS_FIELD))
            builder.actors(mediaJson.get(ACTORS_FIELD).asText());
    }

    private void mapMoviePoster(Message inMessage, Media.MediaBuilder builder) {
        byte[] poster = inMessage.getBody(byte[].class);
        if (poster != null) {
            builder.image(Base64.getEncoder().encodeToString(poster));
        }
    }
}
