package com.wole.eventsapp.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entity){
        super("This " + entity.getSimpleName().toLowerCase() + " does not exist");
    }
}
