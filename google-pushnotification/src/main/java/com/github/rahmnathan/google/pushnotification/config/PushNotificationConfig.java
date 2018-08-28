package com.github.rahmnathan.google.pushnotification.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@ManagedBean
public class PushNotificationConfig {
    private final Logger logger = LoggerFactory.getLogger(PushNotificationConfig.class.getName());
    public static final String GOOGLE_PUSH_NOTIFICATION_ROUTE = "seda:pushnotification";
    private GoogleCredential googleCredential;
    private final CamelContext camelContext;

    public PushNotificationConfig(CamelContext camelContext){
        this.camelContext = camelContext;
    }

    @PostConstruct
    public void configureCamelRoutes(){
        try {
            googleCredential = GoogleCredential
                    .fromStream(new FileInputStream("/opt/localmovie/secrets/localmovie-firebase-key.json"))
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/firebase.messaging"));

            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    onException(HttpOperationFailedException.class)
                            .useExponentialBackOff()
                            .redeliveryDelay(500)
                            .maximumRedeliveries(3)
                            .end();

                    from(GOOGLE_PUSH_NOTIFICATION_ROUTE)
                            .hystrix()
                                .inheritErrorHandler(true)
                                .marshal().json(JsonLibrary.Jackson)
                                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
                                .process(exchange -> exchange.setProperty("accessToken", getAccessToken()))
                                .setHeader(HttpHeaders.AUTHORIZATION, simple("Bearer ${property.accessToken}"))
                                .setHeader(HttpHeaders.CONTENT_TYPE, constant("application/json"))
                                .setHeader(Exchange.HTTP_PATH, constant("/v1/projects/api-9073148299832435984-82417/messages:send"))
                                .to("micrometer:timer:pushnotification-timer?action=start")
                                .to("https4://fcm.googleapis.com")
                                .to("micrometer:timer:pushnotification-timer?action=stop")
                            .endHystrix()
                            .process(new PushNotificationResponseProcessor())
                            .end();
                }
            });
        } catch (Exception e){
            logger.error("Failure adding routes to Camel context", e);
        }
    }

    private String getAccessToken(){
        try {
            googleCredential.refreshToken();
            return googleCredential.getAccessToken();
        } catch (IOException e){
            logger.error("Failure getting access token for Firebase.", e);
            return "";
        }
    }
}
