package com.github.rahmnathan.localmovies.omdb.info.provider;

import com.github.rahmnathan.movie.info.api.IMovieInfoProvider;
import com.github.rahmnathan.movie.info.data.MovieInfo;
import org.imgscalr.Scalr;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OmdbMovieInfoProvider implements IMovieInfoProvider {
    private final Logger logger = Logger.getLogger(OmdbMovieInfoProvider.class.getName());
    private final MovieInfoMapper movieInfoMapper = new MovieInfoMapper();
    private final OmdbRawDataProvider dataProvider;

    @Inject
    public OmdbMovieInfoProvider(OmdbRawDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public MovieInfo loadMovieInfo(String title) {
        JSONObject jsonMovieInfo = dataProvider.loadMovieInfo(title);
        Optional<byte[]> poster = loadPoster(jsonMovieInfo);
        return poster.map(bytes -> movieInfoMapper.jsonToMovieInfo(jsonMovieInfo, title, bytes)).orElseGet(() -> movieInfoMapper.jsonToMovieInfo(jsonMovieInfo, title));

    }

    private Optional<byte[]> loadPoster(JSONObject jsonMovieInfo) {
        try {
            String posterUrl = jsonMovieInfo.getString("Poster");
            Optional<byte[]> poster = dataProvider.loadMoviePoster(posterUrl);
            return poster.flatMap(this::scaleImage);
        } catch (JSONException e) {
            logger.log(Level.SEVERE, "Failed loading poster", e);
        }

        return Optional.empty();
    }

    private Optional<byte[]> scaleImage(byte[] poster) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(poster));
            bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, 200);
            ImageIO.write(bufferedImage, "jpg", outputStream);
            outputStream.flush();
            return Optional.ofNullable(outputStream.toByteArray());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed scaling image", e);
        }

        return Optional.empty();
    }
}