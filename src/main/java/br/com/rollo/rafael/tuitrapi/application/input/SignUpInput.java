package br.com.rollo.rafael.tuitrapi.application.input;

import br.com.rollo.rafael.tuitrapi.application.validators.ValidEmail;
import br.com.rollo.rafael.tuitrapi.application.validators.ValidUsername;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

public class SignUpInput {

    @NotBlank
    @ValidUsername
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @ValidEmail
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
        String password = encoder.encode(this.password);
        return new User(this.username, this.email, password);
    }
}
