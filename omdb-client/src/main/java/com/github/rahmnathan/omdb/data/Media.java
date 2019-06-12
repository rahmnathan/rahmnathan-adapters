package com.github.rahmnathan.omdb.data;

import java.io.Serializable;

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

    public MediaType getMediaType(){
        return mediaType;
    }

    public Integer getNumber(){
        return number;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getMetaRating() {
        return metaRating;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getGenre() {
        return genre;
    }

    public boolean hasMissingValues(){
        return image == null || title == null || imdbRating == null || metaRating == null || actors == null || plot == null
                || releaseYear == null || genre == null || image.isEmpty() || title.isEmpty() || imdbRating.isEmpty()
                || metaRating.isEmpty() || releaseYear.isEmpty() || genre.isEmpty() || actors.isEmpty() || plot.isEmpty();
    }

    @Override
    public String toString() {
        return "Media{" +
                "mediaType=" + mediaType +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                ", metaRating='" + metaRating + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", actors='" + actors + '\'' +
                ", plot='" + plot + '\'' +
                ", genre='" + genre + '\'' +
                ", number=" + number +
                '}';
    }

    public static class Builder {
        private Media media = new Media();

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder setGenre(String genre) {
            media.genre = genre;
            return this;
        }

        public Builder setTitle(String title) {
            media.title = title;
            return this;
        }

        public Builder setIMDBRating(String IMDBRating) {
            media.imdbRating = IMDBRating;
            return this;
        }

        public Builder setMetaRating(String metaRating) {
            media.metaRating = metaRating;
            return this;
        }

        public Builder setImage(String image) {
            media.image = image;
            return this;
        }

        public Builder setReleaseYear(String releaseYear) {
            media.releaseYear = releaseYear;
            return this;
        }

        public Builder setPlot(String plot){
            media.plot = plot;
            return this;
        }

        public Builder setActors(String actors){
            media.actors = actors;
            return this;
        }

        public Builder setNumber(Integer number){
            media.number = number;
            return this;
        }

        public Builder setMediaType(MediaType mediaType){
            media.mediaType = mediaType;
            return this;
        }

        public Media build(){
            Media result = media;
            media = new Media();

            return result;
        }

        public static Media copyWithNewTitle(Media media, String title, Integer number){
            if(media == null)
                return Builder.newInstance().setTitle(title).setNumber(number).build();

            return Builder.newInstance()
                    .setTitle(title)
                    .setReleaseYear(media.getReleaseYear())
                    .setMetaRating(media.getMetaRating())
                    .setIMDBRating(media.getImdbRating())
                    .setImage(media.getImage())
                    .setGenre(media.getGenre())
                    .setActors(media.getActors())
                    .setPlot(media.getPlot())
                    .setNumber(number)
                    .setMediaType(media.getMediaType())
                    .build();
        }

        public static Media copyWithNoImage(Media media){
            if(media == null)
                return Builder.newInstance().build();

            Builder builder = Builder.newInstance()
                    .setTitle(media.getTitle())
                    .setReleaseYear(media.getReleaseYear())
                    .setMetaRating(media.getMetaRating())
                    .setIMDBRating(media.getImdbRating())
                    .setPlot(media.getPlot())
                    .setActors(media.getActors())
                    .setNumber(media.getNumber())
                    .setMediaType(media.getMediaType())
                    .setGenre(media.getGenre());

            if(media.getImage() == null || media.getImage().equals("")){
                builder.setImage("noImage");
            } else {
                builder.setImage("");
            }

            return builder.build();
        }
    }
}