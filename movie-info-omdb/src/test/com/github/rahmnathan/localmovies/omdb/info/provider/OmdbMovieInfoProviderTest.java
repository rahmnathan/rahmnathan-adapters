package com.github.rahmnathan.localmovies.omdb.info.provider;

import com.github.rahmnathan.movie.info.data.MovieInfo;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OmdbMovieInfoProviderTest {
    private static final JSONObject movieInfoJson = TestUtils.getJsonMovieInfo();
    private static OmdbMovieInfoProvider movieInfoProvider;

    @BeforeClass
    public static void initialize(){
        OmdbRawDataProvider rawDataProvider = mock(OmdbRawDataProvider.class);
        when(rawDataProvider.loadMovieInfo("Test")).thenReturn(movieInfoJson);
        when(rawDataProvider.loadMoviePoster("Test")).thenReturn(Optional.empty());
        movieInfoProvider = new OmdbMovieInfoProvider(rawDataProvider);
    }

    @Test
    public void missingPosterTest(){
        MovieInfo movieInfo = movieInfoProvider.loadMovieInfo("Test");

        Assert.assertEquals("10", movieInfo.getIMDBRating());
        Assert.assertEquals("11", movieInfo.getMetaRating());
        Assert.assertEquals("1900", movieInfo.getReleaseYear());
        Assert.assertEquals("Action", movieInfo.getGenre());
    }
}
