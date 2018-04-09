package com.github.rahmnathan.localmovies.omdb.provider.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

@ManagedBean
public class CamelOmdbConfig {
    private final Logger logger = LoggerFactory.getLogger(CamelOmdbConfig.class.getName());
    public static final String OMDB_ROUTE = "direct:omdb";
    private final CamelContext camelContext;

    public CamelOmdbConfig(CamelContext camelContext){
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void configureCamelRoutes(){
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    onException(HttpOperationFailedException.class)
                            .useExponentialBackOff()
                            .backOffMultiplier(2)
                            .redeliveryDelay(1000)
                            .maximumRedeliveries(3)
                            .onWhen(exchange -> exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class).getStatusCode() != 404)
                            .end();

                    from(OMDB_ROUTE)
                            .to("https4://www.omdbapi.com")
                            .end();
                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }
}
