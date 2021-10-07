package com.github.rahmnathan.google.pushnotification.data;

import java.util.HashMap;
import java.util.Map;

public record PushNotification(PushNotificationMessage message) {

    public PushNotificationMessage getMessage() {
        return message;
    }

    public static class Builder {
        private final Map<String, String> notification = new HashMap<>();
        private final Map<String, String> data = new HashMap<>();
        private String topic;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder setTitle(String title) {
            notification.put("title", title);
            data.put("title", title);
            return this;
        }

        public Builder setBody(String body) {
            notification.put("body", body);
            data.put("body", body);
            return this;
        }

        public Builder addData(String key, String value) {
            data.put(key, value);
            return this;
        }

        public PushNotification build() {
            return new PushNotification(new PushNotificationMessage(topic, data, notification));
        }
    }

    @Override
    public String toString() {
        return "PushNotification{" +
                "message=" + message +
                '}';
    }
}
