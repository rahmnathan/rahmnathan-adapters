package com.github.rahmnathan.movie.data;

import java.io.Serializable;

public class Movie implements Serializable {
    private String image;
    private String title;
    private String IMDBRating;
    private String metaRating;
    private String releaseYear;
    private String actors;
    private String plot;
    private String genre;

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getReleaseYear(){
        return releaseYear;
    }

    public String getTitle(){
        return title;
    }

    public String getIMDBRating(){
        return IMDBRating;
    }

    public String getMetaRating(){
        return metaRating;
    }

    public String getImage() {
        return image;
    }

    public String getGenre() {
        return genre;
    }

    public boolean hasMissingValues(){
        return image == null || title == null || IMDBRating == null || metaRating == null || actors == null || plot == null
                || releaseYear == null || genre == null || image.isEmpty() || title.isEmpty() || IMDBRating.isEmpty()
                || metaRating.isEmpty() || releaseYear.isEmpty() || genre.isEmpty() || actors.isEmpty() || plot.isEmpty();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", IMDBRating='" + IMDBRating + '\'' +
                ", metaRating='" + metaRating + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", actors='" + actors + '\'' +
                ", plot='" + plot + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

    public static class Builder {
        private Movie movie = new Movie();

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder setGenre(String genre) {
            movie.genre = genre;
            return this;
        }

        public Builder setTitle(String title) {
            movie.title = title;
            return this;
        }

        public Builder setIMDBRating(String IMDBRating) {
            movie.IMDBRating = IMDBRating;
            return this;
        }

        public Builder setMetaRating(String metaRating) {
            movie.metaRating = metaRating;
            return this;
        }

        public Builder setImage(String image) {
            movie.image = image;
            return this;
        }

        public Builder setReleaseYear(String releaseYear) {
            movie.releaseYear = releaseYear;
            return this;
        }

        public Builder setPlot(String plot){
            movie.plot = plot;
            return this;
        }

        public Builder setActors(String actors){
            movie.actors = actors;
            return this;
        }

        public Movie build(){
            Movie result = movie;
            movie = new Movie();

            return result;
        }

        public static Movie copyWithNewTitle(Movie movie, String title){
            if(movie == null)
                return Builder.newInstance().setTitle(title).build();

            return Builder.newInstance()
                    .setTitle(title)
                    .setReleaseYear(movie.getReleaseYear())
                    .setMetaRating(movie.getMetaRating())
                    .setIMDBRating(movie.getIMDBRating())
                    .setImage(movie.getImage())
                    .setGenre(movie.getGenre())
                    .setActors(movie.getActors())
                    .setPlot(movie.getPlot())
                    .build();
        }

        public static Movie copyWithNoImage(Movie movie){
            if(movie == null)
                return Builder.newInstance().build();

            Builder builder = Builder.newInstance()
                    .setTitle(movie.getTitle())
                    .setReleaseYear(movie.getReleaseYear())
                    .setMetaRating(movie.getMetaRating())
                    .setIMDBRating(movie.getIMDBRating())
                    .setPlot(movie.getPlot())
                    .setActors(movie.getActors())
                    .setGenre(movie.getGenre());

            if(movie.getImage() == null || movie.getImage().equals("")){
                builder.setImage("noImage");
            } else {
                builder.setImage("");
            }

            return builder.build();
        }
    }
}