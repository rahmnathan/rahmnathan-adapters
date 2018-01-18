module keycloak.auth.provider {
    requires spring.context;
    requires java.logging;
    requires camel.core;
    requires spring.beans;
    requires camel.http.common;
    requires javaee.api;
    requires json;
}