module google.pushnotification {
    exports com.github.rahmnathan.google.pushnotification.data;
    exports com.github.rahmnathan.google.pushnotification.boundary;
    requires spring.beans;
    requires camel.core;
    requires javaee.api;
    requires camel.http.common;
    requires slf4j.api;
    requires camel.http4;
}