package com.github.rahmnathan.movie.info.api;

import com.github.rahmnathan.movie.info.data.MovieInfo;

@FunctionalInterface
public interface IMovieInfoProvider {

    MovieInfo loadMovieInfo(String title);
}
