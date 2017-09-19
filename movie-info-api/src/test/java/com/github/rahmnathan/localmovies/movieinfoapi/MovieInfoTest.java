package com.github.rahmnathan.localmovies.movieinfoapi;

import com.github.rahmnathan.movie.info.data.MovieInfo;
import org.junit.Assert;
import org.junit.Test;

public class MovieInfoTest {

    @Test
    public void movieInfoTest(){
        String title = "TestTitle";
        String image = "TestBase64Image";
        String IMDBRating = "TestIMDBRating";
        String metaRating = "TestMetaRating";
        String releaseYear = "TestReleaseYear";

        MovieInfo movieInfo = MovieInfo.Builder.newInstance()
                .setTitle(title)
                .setImage(image)
                .setIMDBRating(IMDBRating)
                .setMetaRating(metaRating)
                .setReleaseYear(releaseYear)
                .build();

        Assert.assertEquals(movieInfo.getTitle(), title);
        Assert.assertEquals(movieInfo.getImage(), image);
        Assert.assertEquals(movieInfo.getIMDBRating(), IMDBRating);
        Assert.assertEquals(movieInfo.getMetaRating(), metaRating);
        Assert.assertEquals(movieInfo.getReleaseYear(), releaseYear);
    }
}