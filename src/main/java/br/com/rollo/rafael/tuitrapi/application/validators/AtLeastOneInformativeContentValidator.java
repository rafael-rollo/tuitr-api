package br.com.rollo.rafael.tuitrapi.application.validators;

import br.com.rollo.rafael.tuitrapi.application.input.PostInput;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class AtLeastOneInformativeContentValidator implements Validator {

    /**
     * In the absense of any MessageSource
     */
    private static final String DEFAULT_MESSAGE = "At least one informational content must be sent.";

    @Override
    public boolean supports(Class<?> clazz) {
        return PostInput.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PostInput input = (PostInput) target;

        List<String> contents = Arrays.asList(
                input.getTextContent(),
                input.getImagePath());

        Predicate<String> notEmpty = content -> content != null && !content.isEmpty();

        if (contents.stream().noneMatch(notEmpty)) {
            rejectValues(errors);
        }
    }

    private void rejectValues(Errors errors) {
        errors.reject("field.required.postInput.textContent", DEFAULT_MESSAGE);
        errors.reject("field.required.postInput.imagePath", DEFAULT_MESSAGE);
    }
}
