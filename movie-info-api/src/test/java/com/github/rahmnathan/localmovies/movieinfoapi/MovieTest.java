package com.github.rahmnathan.localmovies.movieinfoapi;

import com.github.rahmnathan.movie.data.Movie;
import org.junit.Assert;
import org.junit.Test;

public class MovieTest {

    @Test
    public void movieInfoTest(){
        String title = "TestTitle";
        String image = "TestBase64Image";
        String IMDBRating = "TestIMDBRating";
        String metaRating = "TestMetaRating";
        String releaseYear = "TestReleaseYear";

        Movie movie = Movie.Builder.newInstance()
                .setTitle(title)
                .setImage(image)
                .setIMDBRating(IMDBRating)
                .setMetaRating(metaRating)
                .setReleaseYear(releaseYear)
                .build();

        Assert.assertEquals(movie.getTitle(), title);
        Assert.assertEquals(movie.getImage(), image);
        Assert.assertEquals(movie.getIMDBRating(), IMDBRating);
        Assert.assertEquals(movie.getMetaRating(), metaRating);
        Assert.assertEquals(movie.getReleaseYear(), releaseYear);
    }
}