module google.pushnotification {
    exports com.github.rahmnathan.google.pushnotification.data;
    requires spring.context;
    requires spring.beans;
    requires camel.core;
    requires javaee.api;
    requires camel.http.common;
    requires slf4j.api;
}