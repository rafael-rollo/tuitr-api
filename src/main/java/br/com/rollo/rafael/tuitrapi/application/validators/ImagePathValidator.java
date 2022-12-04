package br.com.rollo.rafael.tuitrapi.application.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ImagePathValidator implements ConstraintValidator<ImageResourcePath, String> {

    private static final List<String> REQUIRED_SCHEMES = Arrays.asList("http://", "https://");
    private static final List<String> ACCEPTED_IMAGE_FORMATS = Arrays.asList(".jpg", ".jpeg", ".png");

    @Override
    public boolean isValid(String imagePath, ConstraintValidatorContext context) {
        if (imagePath == null || imagePath.isEmpty()) return true;

        boolean hasRequiredScheme = REQUIRED_SCHEMES.stream().anyMatch(imagePath::startsWith);
        boolean hasAcceptedFileExtension = ACCEPTED_IMAGE_FORMATS.stream().anyMatch(imagePath::endsWith);

        return hasRequiredScheme && hasAcceptedFileExtension;
    }

}
