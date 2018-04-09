package com.github.rahmnathan.movie.api;

import com.github.rahmnathan.movie.data.Movie;

@FunctionalInterface
public interface MovieProvider {

    Movie loadMovieInfo(String title);
}
