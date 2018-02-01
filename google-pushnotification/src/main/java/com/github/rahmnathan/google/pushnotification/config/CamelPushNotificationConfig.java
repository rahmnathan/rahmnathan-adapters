package com.github.rahmnathan.google.pushnotification.config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class CamelPushNotificationConfig {

    @Value(value = "${push.notification.key}")
    private String serverKey;
    private final Logger logger = LoggerFactory.getLogger(CamelPushNotificationConfig.class.getName());
    private final CamelContext camelContext;

    @Inject
    public CamelPushNotificationConfig(CamelContext camelContext){
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
                            .redeliveryDelay(2000)
                            .maximumRedeliveries(2)
                            .end();

                    from("seda:pushnotification")
                            .marshal().json(JsonLibrary.Jackson)
                            .setHeader("Authorization", constant("key=" + serverKey))
                            .setHeader("Content-Type", constant("application/json"))
                            .to("http4://fcm.googleapis.com/fcm/send")
                            .end();
                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }
}
