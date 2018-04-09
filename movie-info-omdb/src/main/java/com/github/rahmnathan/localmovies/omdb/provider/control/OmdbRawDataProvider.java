package com.github.rahmnathan.localmovies.omdb.provider.control;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static com.github.rahmnathan.localmovies.omdb.provider.config.CamelOmdbConfig.OMDB_ROUTE;

@Component
public class OmdbRawDataProvider {
    private final Logger logger = LoggerFactory.getLogger(OmdbRawDataProvider.class.getName());
    private final ProducerTemplate producerTemplate;
    private final String apiKey;

    public OmdbRawDataProvider(ProducerTemplate producerTemplate, @Value("${omdb.api.key}") String apiKey){
        this.producerTemplate = producerTemplate;
        this.apiKey = apiKey;
    }

    public JSONObject loadMovieInfo(String title) {
        String response = producerTemplate.request(OMDB_ROUTE,
                exchange -> exchange.getIn()
                    .setHeader(Exchange.HTTP_QUERY,"t=" + URLEncoder.encode(title, StandardCharsets.UTF_8.name()) + "&apikey=" + apiKey))
                    .getOut()
                    .getBody(String.class);

        logger.info("Title: {} Response: {}", title, response);

        return response != null ? new JSONObject(response) : new JSONObject();
    }

    public Optional<byte[]> loadMoviePoster(String imageURL) {
        byte[] image = producerTemplate.request(OMDB_ROUTE,
                exchange -> exchange.getIn().setHeader(Exchange.HTTP_URI, imageURL))
                .getOut()
                .getBody(byte[].class);

        return Optional.ofNullable(image);
    }
}
