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

public class MediaBuilder implements Processor {
    private static final String IMDB_RATING_FIELD = "imdbRating";
    private static final String META_SCORE_FIELD = "Metascore";
    private static final String ACTORS_FIELD = "Actors";
    private static final String GENRE_FIELD = "Genre";
    private static final String YEAR_FIELD = "Year";
    private static final String PLOT_FIELD = "Plot";
    private final MediaType mediaType;

    public MediaBuilder(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public void process(Exchange exchange) throws MediaProviderException {
        Message inMessage = exchange.getIn();

        JsonNode mediaJson = exchange.getProperty(OMDB_DATA_PROPERTY, JsonNode.class);

        Media.Builder mediaBuilder = Media.Builder.newInstance();
        mediaBuilder.setTitle(exchange.getProperty(MEDIA_TITLE_PROPERTY, String.class));
        mediaBuilder.setMediaType(mediaType);

        if(mediaJson == null || mediaJson.get("Response").asText().equalsIgnoreCase("False")) {
            throw new MediaNotFoundException("Media not found.");
        }

        mapMovieInfo(mediaJson, mediaBuilder);
        mapMoviePoster(inMessage, mediaBuilder);

        if(mediaType == MediaType.EPISODE || mediaType == MediaType.SEASON){
            Integer number = exchange.getProperty(NUMBER_PROPERTY, Integer.class);
            mediaBuilder.setNumber(number);
            if(mediaJson.has("Title")){
                mediaBuilder.setTitle(mediaJson.get("Title").asText());
            } else if(MediaType.EPISODE == mediaType) {
                mediaBuilder.setTitle("Episode " + number);
            } else if(MediaType.SEASON == mediaType){
                mediaBuilder.setTitle("Season " + number);
            }
        }

        exchange.getOut().setBody(mediaBuilder.build());
    }

    private void mapMovieInfo(JsonNode mediaJson, Media.Builder builder) {
        if (mediaJson.has(IMDB_RATING_FIELD))
            builder.setIMDBRating(mediaJson.get(IMDB_RATING_FIELD).asText());
        if (mediaJson.has(META_SCORE_FIELD))
            builder.setMetaRating(mediaJson.get(META_SCORE_FIELD).asText());
        if (mediaJson.has(YEAR_FIELD))
            builder.setReleaseYear(mediaJson.get(YEAR_FIELD).asText());
        if (mediaJson.has(GENRE_FIELD))
            builder.setGenre(mediaJson.get(GENRE_FIELD).asText());
        if (mediaJson.has(PLOT_FIELD))
            builder.setPlot(mediaJson.get(PLOT_FIELD).asText());
        if (mediaJson.has(ACTORS_FIELD))
            builder.setActors(mediaJson.get(ACTORS_FIELD).asText());
    }

    private void mapMoviePoster(Message inMessage, Media.Builder builder) {
        byte[] poster = inMessage.getBody(byte[].class);
        if (poster != null) {
            builder.setImage(Base64.getEncoder().encodeToString(poster));
        }
    }
}
