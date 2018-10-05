package com.github.rahmnathan.google.pushnotification.boundary;

import com.github.rahmnathan.google.pushnotification.config.PushNotificationConfig;
import com.github.rahmnathan.google.pushnotification.data.PushNotification;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class FirebaseNotificationServiceInt extends CamelTestSupport {
    private FirebaseNotificationService notificationService;

    @Before
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
                .setRecipientToken("device-token")
                .setTitle("New Movie!")
                .setBody("Test Body")
                .build();
    }
}
