package com.github.rahmnathan.localmovies.omdb.info.provider;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class OmdbRawDataProvider {

    @Value("${omdb.api.key}")
    private String apiKey;
    private final Logger logger = LoggerFactory.getLogger(OmdbRawDataProvider.class.getName());
    private final ProducerTemplate producerTemplate;

    @Inject
    public OmdbRawDataProvider(ProducerTemplate producerTemplate){
        this.producerTemplate = producerTemplate;
    }

    JSONObject loadMovieInfo(String title) {
        String response = producerTemplate.request("direct:omdb",
                exchange -> exchange.getIn()
                    .setHeader(Exchange.HTTP_QUERY,"t=" + URLEncoder.encode(title, StandardCharsets.UTF_8.name()) + "&apikey=" + apiKey))
                    .getOut()
                    .getBody(String.class);

        logger.info("Title: {} Response: {}", title, response);

        return response != null ? new JSONObject(response) : new JSONObject();
    }

    Optional<byte[]> loadMoviePoster(String imageURL) {
        byte[] image = producerTemplate.request("direct:omdb",
                exchange -> exchange.getIn().setHeader(Exchange.HTTP_URI, imageURL))
                .getOut()
                .getBody(byte[].class);

        return Optional.ofNullable(image);
    }
}
