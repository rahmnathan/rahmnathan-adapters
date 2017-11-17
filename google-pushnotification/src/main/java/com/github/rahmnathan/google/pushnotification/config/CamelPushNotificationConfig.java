package com.github.rahmnathan.google.pushnotification.config;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class CamelPushNotificationConfig {

    @Value(value = "${push.notification.key}")
    private String serverKey;
    private final Logger logger = Logger.getLogger(CamelPushNotificationConfig.class.getName());
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
                public void configure() throws Exception {
                    onException(Exception.class)
                            .useExponentialBackOff()
                            .redeliveryDelay(2000)
                            .maximumRedeliveries(5);

                    from("seda:pushnotification")
                            .marshal().json(JsonLibrary.Jackson)
                            .setHeader("Authorization", constant("key=" + serverKey))
                            .setHeader("Content-Type", constant("application/json"))
                            .to("http4://fcm.googleapis.com/fcm/send");
                }
            });
        } catch (Exception e){
            logger.log(Level.SEVERE, "Failure adding routes to Camel context", e);
        }
    }
}
