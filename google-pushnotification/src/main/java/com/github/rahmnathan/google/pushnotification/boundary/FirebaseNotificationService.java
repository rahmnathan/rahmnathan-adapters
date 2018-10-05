package com.github.rahmnathan.google.pushnotification.boundary;

import com.github.rahmnathan.google.pushnotification.config.PushNotificationConfig;
import com.github.rahmnathan.google.pushnotification.data.PushNotification;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;

import static com.github.rahmnathan.google.pushnotification.config.PushNotificationConfig.GOOGLE_PUSH_NOTIFICATION_ROUTE;

public class FirebaseNotificationService {
    private final ProducerTemplate producerTemplate;

    public FirebaseNotificationService(ProducerTemplate producerTemplate, CamelContext context) {
        this.producerTemplate = producerTemplate;
        new PushNotificationConfig(context).configureCamelRoutes();
    }

    public void sendPushNotification(PushNotification pushNotification){
        producerTemplate.sendBody(GOOGLE_PUSH_NOTIFICATION_ROUTE, pushNotification);
    }
}
