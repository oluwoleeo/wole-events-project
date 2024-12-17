package com.wole.eventsapp.exceptions;

import com.wole.eventsapp.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Error> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        List<Error> errors = new ArrayList<>();

        for (String err: getInvalidArgumentErrors(ex)){
            Error error = new Error();
            error.setErrorMessage(err);

            errors.add(error);
        }

        return errors;
    }

    @ExceptionHandler({EntityNotFoundException.class, InvalidOperationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleOtherErrors(Exception ex){
        Error error = new Error();
        error.setErrorMessage(ex.getMessage());

        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleInternalServerError(Exception ex){
        Error error = new Error();
        error.setErrorMessage(ex.getLocalizedMessage());

        return error;
    }

    private List<String> getInvalidArgumentErrors(MethodArgumentNotValidException ex){
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        List<String> errors = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (ObjectError error: objectErrors){
            errors.add(error.getDefaultMessage());
        }

        return errors;
    }

    private Error error(String message) {
        Error error = new Error();
        error.setErrorMessage(message);
        return error;
    }
}
