package com.github.rahmnathan.google.pushnotification.data;

import java.util.HashMap;
import java.util.Map;

public class PushNotification {
    private final String to;
    private final Map<String, String> data;

    public PushNotification(String to, Map<String, String> data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public static class Builder {
        private String recipientToken;
        private Map<String, String> data = new HashMap<>();

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder setRecipientToken(String recipientToken) {
            this.recipientToken = recipientToken;
            return this;
        }

        public Builder addData(String key, String value){
            data.put(key, value);
            return this;
        }

        public PushNotification build(){
            return new PushNotification(recipientToken, data);
        }
    }
}
