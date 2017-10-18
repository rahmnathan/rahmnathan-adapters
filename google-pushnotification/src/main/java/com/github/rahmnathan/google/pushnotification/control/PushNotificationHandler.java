package com.github.rahmnathan.google.pushnotification.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rahmnathan.google.pushnotification.data.PushNotification;
import com.github.rahmnathan.http.control.HttpClient;
import com.github.rahmnathan.http.data.HttpRequestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PushNotificationHandler {
    private final Logger logger = Logger.getLogger(PushNotificationHandler.class.getName());
    private final String url = "https://fcm.googleapis.com/fcm/send";
    private final String serverKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PushNotificationHandler(String serverKey) {
        this.serverKey = serverKey;
    }

    public void sendPushNotification(PushNotification pushNotification){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "key=" + serverKey);

        try {
            String pushNotificationJson = objectMapper.writeValueAsString(pushNotification);
            headers.put("Content-Length", String.valueOf(pushNotificationJson.getBytes().length));

            String response = HttpClient.getResponseAsString(url, HttpRequestMethod.POST, pushNotificationJson, headers);
            logger.info("PushNotification sent - " + response);
        } catch (JsonProcessingException e){
            logger.severe(e.toString());
        }
    }
}
