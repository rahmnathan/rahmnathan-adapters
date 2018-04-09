package com.github.rahmnathan.movie.data;

import java.io.Serializable;

public class Movie implements Serializable {

    private String image;
    private String title;
    private String IMDBRating;
    private String metaRating;
    private String releaseYear;
    private String genre;

    private Movie(String title, String IMDBRating, String metaRating, String image, String releaseYear, String genre) {
        this.title = title;
        this.IMDBRating = IMDBRating;
        this.metaRating = metaRating;
        this.releaseYear = releaseYear;
        this.image = image;
        this.genre = genre;
    }

    public Movie(){
        // Default constructor
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
        return image == null || title == null || IMDBRating == null || metaRating == null || releaseYear == null || genre == null
                || image.equals("") || title.equals("") || IMDBRating.equals("") || metaRating.equals("") || releaseYear.equals("") || genre.equals("");
    }

    @Override
    public String toString() {
        return "Movie{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", IMDBRating='" + IMDBRating + '\'' +
                ", metaRating='" + metaRating + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }

    public static class Builder {
        private String title;
        private String IMDBRating;
        private String metaRating;
        private String image;
        private String releaseYear;
        private String genre;

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder setGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setIMDBRating(String IMDBRating) {
            this.IMDBRating = IMDBRating;
            return this;
        }

        public Builder setMetaRating(String metaRating) {
            this.metaRating = metaRating;
            return this;
        }

        public Builder setImage(String image) {
            this.image = image;
            return this;
        }

        public Builder setReleaseYear(String releaseYear) {
            this.releaseYear = releaseYear;
            return this;
        }

        public Movie build(){
            return new Movie(title, IMDBRating, metaRating, image, releaseYear, genre);
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