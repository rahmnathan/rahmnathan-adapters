package com.github.rahmnathan.movie.info.data;

import java.io.Serializable;

public class MovieInfo implements Serializable {

    private String image;
    private String title;
    private String IMDBRating;
    private String metaRating;
    private String releaseYear;
    private String genre;

    private MovieInfo(String title, String IMDBRating, String metaRating, String image, String releaseYear, String genre) {
        this.title = title;
        this.IMDBRating = IMDBRating;
        this.metaRating = metaRating;
        this.releaseYear = releaseYear;
        this.image = image;
        this.genre = genre;
    }

    public MovieInfo(){
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
                || image.equals("") || title.equals("") || IMDBRating.equals("") || metaRating.equals("") | releaseYear.equals("") || genre.equals("");
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
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

        public MovieInfo build(){
            return new MovieInfo(title, IMDBRating, metaRating, image, releaseYear, genre);
        }

        public static MovieInfo copyWithNewTitle(MovieInfo movieInfo, String title){
            if(movieInfo == null)
                return Builder.newInstance().setTitle(title).build();

            return Builder.newInstance()
                    .setTitle(title)
                    .setReleaseYear(movieInfo.getReleaseYear())
                    .setMetaRating(movieInfo.getMetaRating())
                    .setIMDBRating(movieInfo.getIMDBRating())
                    .setImage(movieInfo.getImage())
                    .setGenre(movieInfo.getGenre())
                    .build();
        }

        public static MovieInfo copyWithNoImage(MovieInfo movieInfo){
            if(movieInfo == null)
                return Builder.newInstance().build();

            Builder builder = Builder.newInstance()
                    .setTitle(movieInfo.getTitle())
                    .setReleaseYear(movieInfo.getReleaseYear())
                    .setMetaRating(movieInfo.getMetaRating())
                    .setIMDBRating(movieInfo.getIMDBRating())
                    .setGenre(movieInfo.getGenre());

            if(movieInfo.getImage() == null || movieInfo.getImage().equals("")){
                builder.setImage("noImage");
            } else {
                builder.setImage("");
            }

            return builder.build();
        }
    }
}