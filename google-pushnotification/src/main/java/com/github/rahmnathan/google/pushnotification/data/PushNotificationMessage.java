package com.github.rahmnathan.google.pushnotification.data;

import java.util.Map;

public class PushNotificationMessage {
    private final String token;
    private final Map<String, String> notification;
    private final Map<String, String> data;

    public PushNotificationMessage(String token, Map<String, String> data, Map<String, String> notification) {
        this.notification = notification;
        this.data = data;
        this.token = token;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public String getToken() {
        return token;
    }

    public Map<String, String> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PushNotificationMessage{" +
                "token='" + token + '\'' +
                ", notification=" + notification +
                ", data=" + data +
                '}';
    }
}
