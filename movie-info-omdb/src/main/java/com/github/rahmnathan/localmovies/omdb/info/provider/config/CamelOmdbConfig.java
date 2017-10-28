package com.github.rahmnathan.localmovies.omdb.info.provider.config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
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
                public void configure() throws Exception {
                    onException(Exception.class)
                            .useExponentialBackOff()
                            .redeliveryDelay(1000)
                            .maximumRedeliveries(5);

                    from("direct:omdb")
                            .to("http4://www.omdbapi.com");
                }
            });
        } catch (Exception e){
            logger.severe(e.toString());
        }
    }
}
