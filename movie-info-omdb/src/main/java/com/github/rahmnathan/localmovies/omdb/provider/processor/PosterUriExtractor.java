package com.github.rahmnathan.localmovies.omdb.provider.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.io.IOException;

import static com.github.rahmnathan.localmovies.omdb.provider.config.OmdbCamelRoutes.OMDB_DATA_PROPERTY;

public class PosterUriExtractor implements Processor {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String POSTER_FIELD = "Poster";

    @Override
    public void process(Exchange exchange) throws IOException {
        Message inMessage = exchange.getIn();
        String responseBody = inMessage.getBody(String.class);
        if(responseBody != null) {
            JsonNode movieJson = objectMapper.readValue(responseBody, JsonNode.class);
            if (movieJson.has(POSTER_FIELD)) {
                inMessage.setHeader(Exchange.HTTP_URI, movieJson.get(POSTER_FIELD).asText());
            }

            exchange.setProperty(OMDB_DATA_PROPERTY, movieJson);
        }
    }
}
