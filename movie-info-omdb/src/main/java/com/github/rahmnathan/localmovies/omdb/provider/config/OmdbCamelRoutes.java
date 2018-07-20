package com.github.rahmnathan.localmovies.omdb.provider.config;

import com.github.rahmnathan.localmovies.omdb.provider.processor.MovieBuilder;
import com.github.rahmnathan.localmovies.omdb.provider.processor.PosterUriExtractor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OmdbCamelRoutes {
    private final Logger logger = LoggerFactory.getLogger(OmdbCamelRoutes.class.getName());
    public static final String OMDB_MOVIE_ROUTE = "direct:omdbMovie";
    private static final String OMDB_URL = "https4://www.omdbapi.com";
    public static final String MOVIE_TITLE_PROPERTY = "movieTitle";
    public static final String OMDB_DATA_PROPERTY = "omdbData";
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
                            .useExponentialBackOff()
                            .redeliveryDelay(500)
                            .maximumRedeliveries(3)
                            .end();

                    from(OMDB_MOVIE_ROUTE)
                            .to(OMDB_URL)
                            .process(new PosterUriExtractor())
                            .choice()
                                .when(exchange -> exchange.getIn().getHeader(Exchange.HTTP_URI) != null)
                                    .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                                    .to(OMDB_URL)
                            .end()
                            .process(new MovieBuilder())
                            .end();
                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }
}
