package br.com.rollo.rafael.tuitrapi.application.input;

import br.com.rollo.rafael.tuitrapi.application.validators.UniqueEmail;
import br.com.rollo.rafael.tuitrapi.application.validators.UniqueUsername;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.NotBlank;

public class SignUpInput {

    @NotBlank
    @UniqueUsername
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @UniqueEmail
    private String email;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User build(PasswordEncoder encoder) {
        var password = encoder.encode(this.password);
        return new User(this.username, this.email, password);
    }
}
