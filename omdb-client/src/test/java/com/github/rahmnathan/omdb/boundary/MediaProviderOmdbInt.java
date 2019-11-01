package com.github.rahmnathan.omdb.boundary;

import com.github.rahmnathan.omdb.data.Media;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaProviderOmdbInt extends CamelTestSupport {
    private final Logger logger = LoggerFactory.getLogger(MediaProviderOmdbInt.class);
    private final String apiKey = "a825746e";
    private MediaProviderOmdb movieProvider;

    @Before
    public void initialize(){
        this.movieProvider = new MediaProviderOmdb(context, template, apiKey);
    }

    @Test
    public void getMovieInt() throws Exception {
        Media media = movieProvider.getMovie("300");

        logger.info("Response Media: {}", media);
    }

    @Test
    public void getSeasonInt() throws Exception {
        Media media = movieProvider.getSeason("Black Mirror", 1);

        logger.info("Response Media: {}", media);
    }

    @Test
    public void getEpisodeInt() throws Exception {
        Media media = movieProvider.getEpisode("Black Mirror", 1, 1);

        logger.info("Response Media: {}", media);
    }
}
