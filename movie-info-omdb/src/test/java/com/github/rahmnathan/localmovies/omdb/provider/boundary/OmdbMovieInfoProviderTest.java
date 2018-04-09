package com.github.rahmnathan.localmovies.omdb.provider.boundary;

import com.github.rahmnathan.localmovies.omdb.provider.TestUtils;
import com.github.rahmnathan.localmovies.omdb.provider.control.OmdbRawDataProvider;
import com.github.rahmnathan.movie.data.Movie;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OmdbMovieInfoProviderTest {
    private static final JSONObject movieInfoJson = TestUtils.getJsonMovieInfo();
    private static OmdbMovieProvider movieProvider;

    @BeforeClass
    public static void initialize(){
        OmdbRawDataProvider rawDataProvider = mock(OmdbRawDataProvider.class);
        when(rawDataProvider.loadMovieInfo("Test")).thenReturn(movieInfoJson);
        when(rawDataProvider.loadMoviePoster("Test")).thenReturn(Optional.empty());
        movieProvider = new OmdbMovieProvider(rawDataProvider);
    }

    @Test
    public void missingPosterTest(){
        Movie movieInfo = movieProvider.loadMovieInfo("Test");

        Assert.assertEquals("10", movieInfo.getIMDBRating());
        Assert.assertEquals("11", movieInfo.getMetaRating());
        Assert.assertEquals("1900", movieInfo.getReleaseYear());
        Assert.assertEquals("Action", movieInfo.getGenre());
    }
}
