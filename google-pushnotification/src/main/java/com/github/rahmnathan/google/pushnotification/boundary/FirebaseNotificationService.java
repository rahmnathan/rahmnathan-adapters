package com.github.rahmnathan.google.pushnotification.boundary;

import com.github.rahmnathan.google.pushnotification.data.PushNotification;
import org.apache.camel.ProducerTemplate;

import javax.annotation.ManagedBean;

import static com.github.rahmnathan.google.pushnotification.config.PushNotificationConfig.GOOGLE_PUSH_NOTIFICATION_ROUTE;

@ManagedBean
public class FirebaseNotificationService {
    private final ProducerTemplate producerTemplate;

    public FirebaseNotificationService(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public void sendPushNotification(PushNotification pushNotification){
        producerTemplate.sendBody(GOOGLE_PUSH_NOTIFICATION_ROUTE, pushNotification);
    }
}
