package br.com.rollo.rafael.tuitrapi.application.validators;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;

@Component
public class UsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserRepository users;

    @Autowired
    public UsernameValidator(UserRepository users) {
        this.users = users;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        Optional<User> user = this.users.findByUsername(username);
        return user.isEmpty();
    }

}