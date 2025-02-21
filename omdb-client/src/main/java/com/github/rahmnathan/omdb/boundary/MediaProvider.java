package com.github.rahmnathan.omdb.boundary;

import com.github.rahmnathan.omdb.data.Media;
import com.github.rahmnathan.omdb.exception.MediaProviderException;

public interface MediaProvider {

    Media getMovie(String title) throws MediaProviderException;
    Media getSeries(String title) throws MediaProviderException;
    Media getEpisode(String seriesTitle, Integer seasonNumber, Integer episodeNumber) throws MediaProviderException;
}
