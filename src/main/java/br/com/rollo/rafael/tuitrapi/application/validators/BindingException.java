package br.com.rollo.rafael.tuitrapi.application.validators;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BindingException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "An error occurred on binding data. Check the data sent and try again.";

    private List<ObjectError> errors = new ArrayList<>();

    public BindingException(BindingResult result) {
        super();
        this.errors = result.getAllErrors();
    }

    public List<String> getErrorMessages() {
        return errors.stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
    }

    public String getPrimaryErrorMessage() {
        return getErrorMessages().stream().findFirst().orElse(DEFAULT_MESSAGE);
    }

}
