package com.github.rahmnathan.google.pushnotification.data;

import java.util.HashMap;
import java.util.Map;

public class PushNotification {
    private final PushNotificationMessage message;

    public PushNotification(PushNotificationMessage message) {
        this.message = message;
    }

    public PushNotificationMessage getMessage() {
        return message;
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
            return new PushNotification(new PushNotificationMessage(recipientToken, data, notification));
        }
    }

    @Override
    public String toString() {
        return "PushNotification{" +
                "message=" + message +
                '}';
    }
}
