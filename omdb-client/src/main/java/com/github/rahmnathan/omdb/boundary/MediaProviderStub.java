package com.github.rahmnathan.omdb.boundary;

import com.github.rahmnathan.omdb.data.Media;

public class MediaProviderStub implements MediaProvider {

    @Override
    public Media getMovie(String title) {
        return Media.builder().title(title).build();
    }

    @Override
    public Media getSeason(String seriesTitle, Integer seasonNumber) {
        return Media.builder().title(seriesTitle).number(seasonNumber).build();
    }

    @Override
    public Media getEpisode(String seriesTitle, Integer seasonNumber, Integer episodeNumber) {
        return Media.builder().title(seriesTitle).number(episodeNumber).build();
    }
}
