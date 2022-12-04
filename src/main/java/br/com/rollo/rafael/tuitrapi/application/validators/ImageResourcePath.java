package br.com.rollo.rafael.tuitrapi.application.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImagePathValidator.class)
public @interface ImageResourcePath {
    String message() default
            "{br.com.rollo.rafael.tuitrapi.application.validators.ImagePathValidator.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
