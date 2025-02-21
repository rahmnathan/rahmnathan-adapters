package com.github.rahmnathan.omdb.boundary;

import com.github.rahmnathan.omdb.data.Media;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediaProviderOmdbInt extends CamelTestSupport {
    private final Logger logger = LoggerFactory.getLogger(MediaProviderOmdbInt.class);
    private final String apiKey = "key";
    private MediaProviderOmdb movieProvider;

    @BeforeEach
    public void initialize(){
        this.movieProvider = new MediaProviderOmdb(context, template, apiKey);
    }

    @Test
    public void getMovieInt() throws Exception {
        Media media = movieProvider.getMovie("300");

        logger.info("Response Media: {}", media);
    }

    @Test
    public void getSeriesInt() throws Exception {
        Media media = movieProvider.getSeries("Black Mirror");

        logger.info("Response Media: {}", media);
    }

    @Test
    public void getEpisodeInt() throws Exception {
        Media media = movieProvider.getEpisode("Black Mirror", 1, 1);

        logger.info("Response Media: {}", media);
    }
}
