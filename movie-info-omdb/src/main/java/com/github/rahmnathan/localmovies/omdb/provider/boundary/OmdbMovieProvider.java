package com.github.rahmnathan.localmovies.omdb.provider.boundary;

import com.github.rahmnathan.localmovies.omdb.provider.control.OmdbRawDataProvider;
import com.github.rahmnathan.movie.api.MovieProvider;
import com.github.rahmnathan.movie.data.Movie;
import org.imgscalr.Scalr;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import static com.github.rahmnathan.localmovies.omdb.provider.control.MovieInfoMapper.jsonToMovieInfo;

@Component
public class OmdbMovieProvider implements MovieProvider {
    private final Logger logger = LoggerFactory.getLogger(OmdbMovieProvider.class.getName());
    private final OmdbRawDataProvider dataProvider;

    @Inject
    public OmdbMovieProvider(OmdbRawDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Movie loadMovieInfo(String title) {
        JSONObject jsonMovieInfo = dataProvider.loadMovieInfo(title);
        Optional<byte[]> poster = loadPoster(jsonMovieInfo);
        return poster.map(bytes -> jsonToMovieInfo(jsonMovieInfo, title, bytes)).orElseGet(() -> jsonToMovieInfo(jsonMovieInfo, title));

    }

    private Optional<byte[]> loadPoster(JSONObject jsonMovieInfo) {
        try {
            String posterUrl = jsonMovieInfo.getString("Poster");
            return dataProvider.loadMoviePoster(posterUrl);
        } catch (JSONException e) {
            logger.error("Failed loading poster", e);
        }

        return Optional.empty();
    }
}