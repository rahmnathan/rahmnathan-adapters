//package com.github.rahmnathan.google.pushnotification.boundary;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.rahmnathan.google.pushnotification.config.PushNotificationConfig;
//import com.github.rahmnathan.google.pushnotification.data.PushNotification;
//import org.apache.camel.test.junit4.CamelTestSupport;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class FirebaseNotificationServiceTest extends CamelTestSupport {
//    private final Logger logger = LoggerFactory.getLogger(FirebaseNotificationServiceTest.class);
//    private FirebaseNotificationService notificationService;
//
//    @Before
//    public void initialize(){
//        new PushNotificationConfig(context, "server-key").configureCamelRoutes();
//        notificationService = new FirebaseNotificationService(template);
//    }
//
//    @Test
//    @Ignore
//    public void sendNotificationTest() throws Exception {
//        PushNotification notification = buildNotification();
//        logger.info(new ObjectMapper().writeValueAsString(notification));
//        notificationService.sendPushNotification(buildNotification());
//    }
//
//    private PushNotification buildNotification(){
//        return PushNotification.Builder.newInstance()
//                .setRecipientToken("device-token")
//                .addData("Title", "New Movie!")
//                .addData("Body", "Test Body")
//                .build();
//    }
//}
