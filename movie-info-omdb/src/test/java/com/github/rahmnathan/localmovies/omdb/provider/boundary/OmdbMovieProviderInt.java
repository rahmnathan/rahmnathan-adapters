package com.github.rahmnathan.localmovies.omdb.provider.boundary;

import com.github.rahmnathan.localmovies.omdb.provider.data.Movie;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OmdbMovieProviderInt extends CamelTestSupport {
    private final Logger logger = LoggerFactory.getLogger(OmdbMovieProviderInt.class);
    private final String apiKey = "";
    private OmdbMovieProvider movieProvider;

    @Before
    public void initialize(){
        this.movieProvider = new OmdbMovieProvider(context, template, apiKey);
    }

    @Test
    public void getMovieInt() throws Exception {
        Movie movie = movieProvider.getMovie("300");

        logger.info("Response Movie: {}", movie);
    }
}
