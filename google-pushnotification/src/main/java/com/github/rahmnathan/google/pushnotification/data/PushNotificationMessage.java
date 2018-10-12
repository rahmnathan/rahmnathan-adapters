package com.github.rahmnathan.google.pushnotification.data;

import java.util.Map;

public class PushNotificationMessage {
    private final String topic;
    private final Map<String, String> notification;
    private final Map<String, String> data;

    public PushNotificationMessage(String topic, Map<String, String> data, Map<String, String> notification) {
        this.notification = notification;
        this.data = data;
        this.topic = topic;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public String getTopic() {
        return topic;
    }

    public Map<String, String> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "PushNotificationMessage{" +
                "topic='" + topic + '\'' +
                ", notification=" + notification +
                ", data=" + data +
                '}';
    }
}
