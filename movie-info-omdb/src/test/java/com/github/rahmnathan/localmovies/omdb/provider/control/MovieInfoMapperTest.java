package com.github.rahmnathan.localmovies.omdb.provider.control;

import com.github.rahmnathan.localmovies.omdb.provider.TestUtils;
import com.github.rahmnathan.movie.data.Movie;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static com.github.rahmnathan.localmovies.omdb.provider.control.MovieInfoMapper.jsonToMovieInfo;

public class MovieInfoMapperTest {
    private static final JSONObject jsonObject = TestUtils.getJsonMovieInfo();
    private final String title = "Title";
    private final byte[] image = new byte[0];

    @Test
    public void jsonToMovieInfoTest(){
        Movie movieInfo = jsonToMovieInfo(jsonObject, title, image);
        Assert.assertEquals("10", movieInfo.getIMDBRating());
        Assert.assertEquals("11", movieInfo.getMetaRating());
        Assert.assertEquals("1900", movieInfo.getReleaseYear());
        Assert.assertEquals("Action", movieInfo.getGenre());
        Assert.assertEquals(title, movieInfo.getTitle());
    }

    @Test
    public void testNullImage(){
        Movie movieInfo = jsonToMovieInfo(jsonObject, title, null);
        Assert.assertNull(movieInfo.getImage());
    }

    @Test
    public void testWithAbsentImage(){
        Movie movieInfo = jsonToMovieInfo(jsonObject, title);
        Assert.assertNull(movieInfo.getImage());
    }
}
