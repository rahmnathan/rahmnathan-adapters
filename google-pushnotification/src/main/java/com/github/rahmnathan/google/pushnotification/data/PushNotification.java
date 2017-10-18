package com.github.rahmnathan.google.pushnotification.data;

public class PushNotification {
    private final String to;
    private final String title;
    private final String body;

    public PushNotification(String to, String title, String body) {
        this.to = to;
        this.title = title;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public static class Builder {
        private String recipientToken;
        private String notificationTitle;
        private String notificationBody;

        public static Builder newInstance(){
            return new Builder();
        }

        public Builder setRecipientToken(String recipientToken) {
            this.recipientToken = recipientToken;
            return this;
        }

        public Builder setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
            return this;
        }

        public Builder setNotificationBody(String notificationBody) {
            this.notificationBody = notificationBody;
            return this;
        }

        public PushNotification build(){
            return new PushNotification(recipientToken, notificationTitle, notificationBody);
        }
    }
}
