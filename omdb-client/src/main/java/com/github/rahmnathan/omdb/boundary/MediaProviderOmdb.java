package com.github.rahmnathan.omdb.boundary;

import com.github.rahmnathan.omdb.data.Media;
import com.github.rahmnathan.omdb.exception.MediaProviderException;
import com.github.rahmnathan.omdb.config.OmdbCamelRoutes;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MediaProviderOmdb implements MediaProvider {
    private final Logger logger = LoggerFactory.getLogger(MediaProviderOmdb.class.getName());
    private final ProducerTemplate template;
    private final String apiKey;

    public MediaProviderOmdb(CamelContext context, ProducerTemplate template, String apiKey) {
        new OmdbCamelRoutes(context).initialize();
        this.template = template;
        this.apiKey = apiKey;
    }

    public Media getMovie(String title) throws MediaProviderException {
        logger.debug("Request for media: {}", title);

        Exchange responseExchange = template.request(OmdbCamelRoutes.OMDB_MOVIE_ROUTE, exchange -> {
            exchange.setProperty(OmdbCamelRoutes.MEDIA_TITLE_PROPERTY, title);
            exchange.getIn().setHeader(Exchange.HTTP_QUERY,
                    "t=" + URLEncoder.encode(title, StandardCharsets.UTF_8) +
                            "&apikey=" + apiKey+
                            "&type=movie");
        });

        Media media = parseResponse(responseExchange, title);

        logger.debug("Movie response: {}", media);
        return media;
    }

    public Media getSeries(String title) throws MediaProviderException {
        logger.debug("Received request for series: {}", title);

        Exchange responseExchange = template.request(OmdbCamelRoutes.OMDB_SEASON_ROUTE, exchange -> {
            exchange.setProperty(OmdbCamelRoutes.MEDIA_TITLE_PROPERTY, title);
            exchange.getIn().setHeader(Exchange.HTTP_QUERY,
                    "t=" + URLEncoder.encode(title, StandardCharsets.UTF_8) +
                            "&apikey=" + apiKey +
                            "&type=series");
        });

        Media media = parseResponse(responseExchange, title);

        logger.debug("Series response: {}", media);
        return media;
    }

    public Media getEpisode(String seriesTitle, Integer seasonNumber, Integer episodeNumber) throws MediaProviderException {
        logger.debug("Received request for episode. Series: {} season number: {} episode number: {}", seriesTitle, seasonNumber, episodeNumber);

        Exchange responseExchange = template.request(OmdbCamelRoutes.OMDB_EPISODE_ROUTE, exchange -> {
            exchange.setProperty(OmdbCamelRoutes.MEDIA_TITLE_PROPERTY, seriesTitle);
            exchange.setProperty(OmdbCamelRoutes.NUMBER_PROPERTY, episodeNumber);
            exchange.getIn().setHeader(Exchange.HTTP_QUERY,
                    "t=" + URLEncoder.encode(seriesTitle, StandardCharsets.UTF_8) +
                            "&Season=" + seasonNumber +
                            "&Episode=" + episodeNumber +
                            "&apikey=" + apiKey +
                            "&type=episode");
        });

        Media media = parseResponse(responseExchange, seriesTitle);

        logger.debug("Episode response: {}", media);
        return media;
    }

    private Media parseResponse(Exchange exchange, String title) throws MediaProviderException {
        MediaProviderException mediaProviderException = exchange.getException(MediaProviderException.class);
        if(mediaProviderException != null){
            throw mediaProviderException;
        }

        HttpOperationFailedException httpException = exchange.getException(HttpOperationFailedException.class);
        if(httpException != null){
            throw new MediaProviderException("Received null response from omdb for title: " + title, httpException);
        }

        Media media = exchange.getMessage().getBody(Media.class);

        if(media == null){
            throw new MediaProviderException("Received null response from omdb for title: " + title);
        }

        return media;
    }
}