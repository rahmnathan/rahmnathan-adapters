package com.github.rahmnathan.google.pushnotification.data;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class PushNotificationMessage {
    private final String topic;
    private final Map<String, String> notification;
    private final Map<String, String> data;
}
