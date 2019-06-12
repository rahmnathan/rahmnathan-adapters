package com.github.rahmnathan.omdb.exception;

public class MediaNotFoundException extends MediaProviderException {
    public MediaNotFoundException(String message){
        super(message);
    }
}
