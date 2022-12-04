package br.com.rollo.rafael.tuitrapi.application.advice;

import br.com.rollo.rafael.tuitrapi.application.output.APIError;
import br.com.rollo.rafael.tuitrapi.application.validators.BindingException;
import br.com.rollo.rafael.tuitrapi.domain.follows.BroadcastUserUnfollowException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindingException.class)
    public ResponseEntity<APIError> processBindingFailure(BindingException exception) {
        APIError error = new APIError(exception.getPrimaryErrorMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BroadcastUserUnfollowException.class)
    public ResponseEntity<APIError> processInvalidUnfollow(BroadcastUserUnfollowException exception) {
        APIError error = new APIError(exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

}
