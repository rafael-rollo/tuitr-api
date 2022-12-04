package br.com.rollo.rafael.tuitrapi.application.validators;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class EmailValidator implements ConstraintValidator<UniqueEmail, String> {

    static final String REGEX_PATTERN = "^[\\w\\d]([-_.]?[\\w\\d])*@[\\w\\d]([-.]?[\\w\\d])*\\.([a-z]{2,20})$";

    private UserRepository users;

    @Autowired
    public EmailValidator(UserRepository users) {
        this.users = users;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<User> user = users.findByEmail(email);
        return ! user.isPresent();
    }
}