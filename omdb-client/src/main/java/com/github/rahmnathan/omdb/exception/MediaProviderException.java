package com.github.rahmnathan.omdb.exception;

public class MediaProviderException extends Exception {

    public MediaProviderException(String s){
        super(s);
    }

    public MediaProviderException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
