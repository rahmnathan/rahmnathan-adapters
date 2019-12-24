package com.github.rahmnathan.google.pushnotification.boundary;

import com.github.rahmnathan.google.pushnotification.config.PushNotificationConfig;
import com.github.rahmnathan.google.pushnotification.data.PushNotification;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FirebaseNotificationServiceInt extends CamelTestSupport {
    private FirebaseNotificationService notificationService;

    @BeforeEach
    public void initialize(){
        new PushNotificationConfig(context).configureCamelRoutes();
        notificationService = new FirebaseNotificationService(template, context);
    }

    @Test
    public void sendNotificationInt() {
        notificationService.sendPushNotification(buildNotification());
    }

    private PushNotification buildNotification(){
        return PushNotification.Builder.newInstance()
                .setTopic("device-token")
                .setTitle("New Movie!")
                .setBody("Test Body")
                .build();
    }
}
