package com.github.rahmnathan.localmovies.omdb.info.provider.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CamelOmdbConfig {
    private final Logger logger = Logger.getLogger(CamelOmdbConfig.class.getName());
    private final CamelContext camelContext;

    @Inject
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

                    from("direct:omdb")
                            .to("http4://www.omdbapi.com")
                            .end();
                }
            });
        } catch (Exception e){
            logger.log(Level.SEVERE, "Failure adding routes to Camel context", e);
        }
    }
}
