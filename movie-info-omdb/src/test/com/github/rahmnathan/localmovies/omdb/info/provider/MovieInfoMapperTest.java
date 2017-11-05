package com.github.rahmnathan.localmovies.omdb.info.provider;

import com.github.rahmnathan.movie.info.data.MovieInfo;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class MovieInfoMapperTest {
    private final MovieInfoMapper movieInfoMapper = new MovieInfoMapper();
    private static final JSONObject jsonObject = TestUtils.getJsonMovieInfo();
    private final String title = "Title";
    private final byte[] image = new byte[0];

    @Test
    public void jsonToMovieInfoTest(){
        MovieInfo movieInfo = movieInfoMapper.jsonToMovieInfo(jsonObject, title, image);
        Assert.assertEquals("10", movieInfo.getIMDBRating());
        Assert.assertEquals("11", movieInfo.getMetaRating());
        Assert.assertEquals("1900", movieInfo.getReleaseYear());
        Assert.assertEquals("Action", movieInfo.getGenre());
        Assert.assertEquals(title, movieInfo.getTitle());
    }

    @Test
    public void testNullImage(){
        MovieInfo movieInfo = movieInfoMapper.jsonToMovieInfo(jsonObject, title, null);
        Assert.assertNull(movieInfo.getImage());
    }

    @Test
    public void testWithAbsentImage(){
        MovieInfo movieInfo = movieInfoMapper.jsonToMovieInfo(jsonObject, title);
        Assert.assertNull(movieInfo.getImage());
    }
}
