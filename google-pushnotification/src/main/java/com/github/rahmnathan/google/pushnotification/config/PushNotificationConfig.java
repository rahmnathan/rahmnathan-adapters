package com.github.rahmnathan.google.pushnotification.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;

@ManagedBean
public class PushNotificationConfig {
    private final Logger logger = LoggerFactory.getLogger(PushNotificationConfig.class.getName());
    public static final String GOOGLE_PUSH_NOTIFICATION_ROUTE = "seda:pushnotification";
    private final CamelContext camelContext;
    private final String serverKey;

    public PushNotificationConfig(CamelContext camelContext, @Value(value = "${push.notification.key}") String serverKey){
        this.camelContext = camelContext;
        this.serverKey = serverKey;
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

                    from(GOOGLE_PUSH_NOTIFICATION_ROUTE)
                            .marshal().json(JsonLibrary.Jackson)
                            .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
                            .setHeader("Authorization", constant("key=" + serverKey))
                            .setHeader("Content-Type", constant("application/json"))
                            .to("https4://fcm.googleapis.com/fcm/send")
                            .process(new PushNotificationResponseProcessor())
                            .end();
                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }
}
