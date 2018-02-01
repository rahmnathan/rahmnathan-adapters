package com.github.rahmnathan.keycloak.auth.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ws.rs.HttpMethod;

@ManagedBean
public class AccessTokenConfig {
    private final Logger logger = LoggerFactory.getLogger(AccessTokenConfig.class.getName());
    private final CamelContext camelContext;

    public AccessTokenConfig(CamelContext camelContext){
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void initialize(){
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    onException(HttpOperationFailedException.class)
                            .useExponentialBackOff()
                            .backOffMultiplier(2)
                            .redeliveryDelay(500)
                            .maximumRedeliveries(3)
                            .end();

                    from("direct:accesstoken")
                            .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.POST))
                            .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
                            .to("https4://localmovies-cloud.hopto.org/auth/realms/LocalMovies/protocol/openid-connect/token")
                            .end();
                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }
}
