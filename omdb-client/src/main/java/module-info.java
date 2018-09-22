module movie.info.omdb {
    exports com.github.rahmnathan.omdb.exception;
    exports com.github.rahmnathan.omdb.boundary;
    exports com.github.rahmnathan.omdb.data;
    requires slf4j.api;
    requires camel.core;
    requires camel.http.common;
    requires com.fasterxml.jackson.databind;
    requires camel.http4;
}