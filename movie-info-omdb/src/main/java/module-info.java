module movie.info.omdb {
    exports com.github.rahmnathan.localmovies.omdb.info.provider;
    requires spring.context;
    requires java.logging;
    requires camel.core;
    requires javaee.api;
    requires camel.http.common;
    requires movie.info.api;
    requires json;
    requires java.desktop;
    requires imgscalr.lib;
    requires spring.beans;
}