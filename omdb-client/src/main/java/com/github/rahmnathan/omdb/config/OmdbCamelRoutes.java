package com.github.rahmnathan.omdb.config;

import com.github.rahmnathan.omdb.data.MediaType;
import com.github.rahmnathan.omdb.processor.MediaBuilder;
import com.github.rahmnathan.omdb.processor.PosterUriExtractor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.http.common.HttpMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OmdbCamelRoutes {
    private final Logger logger = LoggerFactory.getLogger(OmdbCamelRoutes.class.getName());
    public static final String OMDB_MOVIE_ROUTE = "direct:omdbMovie";
    public static final String OMDB_SEASON_ROUTE = "direct:omdbSeason";
    public static final String OMDB_EPISODE_ROUTE = "direct:omdbEpisode";
    public static final String OMDB_BASE_ROUTE = "direct:omdbBase";

    private static final String OMDB_URL = "https://www.omdbapi.com";
    public static final String MEDIA_TITLE_PROPERTY = "movieTitle";
    public static final String OMDB_DATA_PROPERTY = "omdbData";
    public static final String NUMBER_PROPERTY = "numberProperty";
    private final CamelContext camelContext;

    public OmdbCamelRoutes(CamelContext camelContext){
        this.camelContext = camelContext;
    }

    public void initialize(){
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    onException(HttpOperationFailedException.class)
                            .to("micrometer:timer:omdb-poster-timer?action=stop")
                            .to("micrometer:timer:omdb-data-timer?action=stop")
                            .onWhen(exchange -> exchange.getException(HttpOperationFailedException.class).getStatusCode() >= 500)
                            .useExponentialBackOff()
                            .redeliveryDelay(500)
                            .maximumRedeliveries(3)
                            .end();

                    from(OMDB_SEASON_ROUTE)
                            .to(OMDB_BASE_ROUTE)
                            .process(new MediaBuilder(MediaType.SEASON))
                            .end();

                    from(OMDB_EPISODE_ROUTE)
                            .to(OMDB_BASE_ROUTE)
                            .process(new MediaBuilder(MediaType.EPISODE))
                            .end();

                    from(OMDB_MOVIE_ROUTE)
                            .to(OMDB_BASE_ROUTE)
                            .process(new MediaBuilder(MediaType.MOVIE))
                            .end();

                    from(OMDB_BASE_ROUTE)
                            .to("micrometer:timer:omdb-data-timer?action=start")
                            .to(OMDB_URL)
                            .to("micrometer:timer:omdb-data-timer?action=stop")
                            .removeHeader(Exchange.HTTP_QUERY)
                            .process(new PosterUriExtractor())
                            .choice()
                                .when(exchange -> {
                                        String posterUri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
                                        return posterUri != null && !posterUri.equalsIgnoreCase("n/a");
                                    })
                                    .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                                    .to("micrometer:timer:omdb-poster-timer?action=start")
                                    .doTry()
                                        .to(OMDB_URL)
                                    .doCatch(HttpOperationFailedException.class)
                                        .log(exceptionMessage().toString())
                                    .endDoTry()
                                    .to("micrometer:timer:omdb-poster-timer?action=stop")
                            .end()
                            .end();
                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }
}
