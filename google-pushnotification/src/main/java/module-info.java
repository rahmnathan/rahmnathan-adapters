module google.pushnotification {
    exports com.github.rahmnathan.google.pushnotification.data;
    exports com.github.rahmnathan.google.pushnotification.boundary;
    exports com.github.rahmnathan.google.pushnotification.config;
    requires camel.core;
    requires slf4j.api;
    requires google.api.client;
    requires camel.http.common;
    requires camel.http4;
    requires httpcore;
}