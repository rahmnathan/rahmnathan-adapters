package com.github.rahmnathan.google.pushnotification.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.http.common.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class PushNotificationConfig {
    private final Logger logger = LoggerFactory.getLogger(PushNotificationConfig.class.getName());
    public static final String GOOGLE_PUSH_NOTIFICATION_ROUTE = "seda:pushnotification";
    private final CamelContext camelContext;

    public PushNotificationConfig(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public void configureCamelRoutes() {
        try {
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    onException(HttpOperationFailedException.class)
                            .useExponentialBackOff()
                            .redeliveryDelay(500)
                            .maximumRedeliveries(3)
                            .end();

                    from(GOOGLE_PUSH_NOTIFICATION_ROUTE)
                            .circuitBreaker()
                                .resilience4jConfiguration()
                                    .timeoutDuration(5000)
                                    .timeoutEnabled(true)
                                .end()
                                .inheritErrorHandler(true)
                                .marshal().json(JsonLibrary.Jackson)
                                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
                                .setHeader("Authorization", () -> buildAuthorizationHeader())
                                .setHeader("Content-Type", constant("application/json"))
                                .setHeader(Exchange.HTTP_PATH, constant("/v1/projects/api-9073148299832435984-82417/messages:send"))
                                .to("micrometer:timer:pushnotification-timer?action=start")
                                .to("https://fcm.googleapis.com")
                                .to("micrometer:timer:pushnotification-timer?action=stop")
                            .endCircuitBreaker()
                            .process(new PushNotificationResponseProcessor())
                            .end();
                }
            });
        } catch (Exception e) {
            logger.error("Failure adding routes to Camel context", e);
        }
    }

    private String buildAuthorizationHeader() {
        try {
            GoogleCredential googleCredential = GoogleCredential
                    .fromStream(new FileInputStream("/workspace/secrets/google-services.json"))
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/firebase.messaging"));

            googleCredential.refreshToken();

            return "Bearer " + googleCredential.getAccessToken();
        } catch (IOException e){
            logger.error("Failure acquiring AccessToken.", e);
            return "";
        }
    }

}
