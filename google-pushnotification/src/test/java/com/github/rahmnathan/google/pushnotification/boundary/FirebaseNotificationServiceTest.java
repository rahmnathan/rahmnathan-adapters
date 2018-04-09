package com.github.rahmnathan.google.pushnotification.boundary;

import com.github.rahmnathan.google.pushnotification.config.PushNotificationConfig;
import com.github.rahmnathan.google.pushnotification.data.PushNotification;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class FirebaseNotificationServiceTest extends CamelTestSupport {
    private FirebaseNotificationService notificationService;

    @Before
    public void initialize(){
        new PushNotificationConfig(context, "server-key").configureCamelRoutes();
        notificationService = new FirebaseNotificationService(template);
    }

    @Test
    @Ignore
    public void sendNotificationTest() {
        notificationService.sendPushNotification(buildNotification());
    }

    private PushNotification buildNotification(){
        return PushNotification.Builder.newInstance()
                .setRecipientToken("device-token")
                .addData("title", "New Movie!")
                .addData("body", "Test Body")
                .build();
    }
}
