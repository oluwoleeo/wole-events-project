package com.wole.eventsapp.exceptions;

public class InvalidOperationException extends RuntimeException{
    public InvalidOperationException(final String errorMessage){
        super(errorMessage);
    }
}
