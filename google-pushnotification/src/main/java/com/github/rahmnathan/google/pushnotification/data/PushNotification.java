package com.github.rahmnathan.google.pushnotification.data;

import java.util.HashMap;
import java.util.Map;

public class PushNotification {
    private final String to;
    private final Map<String, String> notification;
    private final Map<String, String> data;

    public PushNotification(String to, Map<String, String> data, Map<String, String> notification) {
        this.notification = notification;
        this.data = data;
        this.to = to;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public String getTo() {
        return to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public static class Builder {
        private Map<String, String> notification = new HashMap<>();
        private Map<String, String> data = new HashMap<>();
        private String recipientToken;

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder setRecipientToken(String recipientToken) {
            this.recipientToken = recipientToken;
            return this;
        }

        public Builder setTitle(String title){
            notification.put("title", title);
            data.put("title", title);
            return this;
        }

        public Builder setBody(String body){
            notification.put("body", body);
            data.put("body", body);
            return this;
        }

        public Builder addData(String key, String value){
            data.put(key, value);
            return this;
        }

        public PushNotification build(){
            return new PushNotification(recipientToken, data, notification);
        }
    }
}
