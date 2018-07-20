package com.github.rahmnathan.localmovies.omdb.boundary;

import com.github.rahmnathan.localmovies.omdb.data.Movie;
import com.github.rahmnathan.localmovies.omdb.exception.MovieProviderException;
import com.github.rahmnathan.localmovies.omdb.config.OmdbCamelRoutes;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.github.rahmnathan.localmovies.omdb.config.OmdbCamelRoutes.MOVIE_TITLE_PROPERTY;
import static com.github.rahmnathan.localmovies.omdb.config.OmdbCamelRoutes.OMDB_MOVIE_ROUTE;

public class OmdbMovieProvider {
    private final Logger logger = LoggerFactory.getLogger(OmdbMovieProvider.class.getName());
    private final ProducerTemplate template;
    private final String apiKey;

    public OmdbMovieProvider(CamelContext context, ProducerTemplate template, String apiKey) {
        new OmdbCamelRoutes(context).initialize();
        this.template = template;
        this.apiKey = apiKey;
    }

    public Movie getMovie(String title) throws MovieProviderException {
        logger.info("Received request for title: {}", title);

        Exchange responseExchange = template.request(OMDB_MOVIE_ROUTE, exchange -> {
            exchange.setProperty(MOVIE_TITLE_PROPERTY, title);
            exchange.getIn().setHeader(Exchange.HTTP_QUERY, "t=" + URLEncoder.encode(title, StandardCharsets.UTF_8.name()) + "&apikey=" + apiKey);
        });

        Movie movie = parseResponse(responseExchange, title);

        logger.debug("Request for title: {} Response: {}", title, movie);

        return movie;
    }

    private Movie parseResponse(Exchange exchange, String title) throws MovieProviderException {
        Movie movie = exchange.getOut().getBody(Movie.class);

        if(movie == null){
            HttpOperationFailedException exception = exchange.getException(HttpOperationFailedException.class);
            throw new MovieProviderException("Received null response from omdb for title: " + title, exception);
        }

        return movie;
    }
}