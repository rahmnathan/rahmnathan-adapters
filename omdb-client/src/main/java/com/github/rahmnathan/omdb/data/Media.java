package com.github.rahmnathan.omdb.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Media implements Serializable {
    private MediaType mediaType;
    private String image;
    private String title;
    private String imdbRating;
    private String metaRating;
    private String releaseYear;
    private String actors;
    private String plot;
    private String genre;
    private Integer number;

    public boolean hasMissingValues() {
        return image == null || title == null || imdbRating == null || metaRating == null || actors == null || plot == null
                || releaseYear == null || genre == null || image.isEmpty() || title.isEmpty() || imdbRating.isEmpty()
                || metaRating.isEmpty() || releaseYear.isEmpty() || genre.isEmpty() || actors.isEmpty() || plot.isEmpty();
    }

    public static Media copyWithNewTitleNumberAndType(Media media, String title, Integer number, MediaType mediaType) {
        if (media == null)
            return Media.builder()
                    .title(title)
                    .number(number)
                    .mediaType(mediaType)
                    .build();

        return Media.builder()
                .title(title)
                .releaseYear(media.getReleaseYear())
                .metaRating(media.getMetaRating())
                .imdbRating(media.getImdbRating())
                .image(media.getImage())
                .genre(media.getGenre())
                .actors(media.getActors())
                .plot(media.getPlot())
                .number(number)
                .mediaType(mediaType)
                .build();
    }

    public static Media copyWithNoImage(Media media) {
        if (media == null)
            return Media.builder().build();

        MediaBuilder builder = Media.builder()
                .title(media.getTitle())
                .releaseYear(media.getReleaseYear())
                .metaRating(media.getMetaRating())
                .imdbRating(media.getImdbRating())
                .plot(media.getPlot())
                .actors(media.getActors())
                .number(media.getNumber())
                .mediaType(media.getMediaType())
                .genre(media.getGenre());

        if (media.getImage() == null || media.getImage().equals("")) {
            builder.image("noImage");
        } else {
            builder.image("");
        }

        return builder.build();
    }
}