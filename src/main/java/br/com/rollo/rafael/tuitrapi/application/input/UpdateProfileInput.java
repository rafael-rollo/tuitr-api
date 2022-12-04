package br.com.rollo.rafael.tuitrapi.application.input;

import br.com.rollo.rafael.tuitrapi.application.validators.UniqueUsername;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.time.LocalDate;

public class UpdateProfileInput {

    @UniqueUsername
    private String username;

    @Length(max = 70)
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    private LocalDate birthDate;
    private String location;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User build() {
        User user = new User(this.username);
        user.setFullName(this.fullName);
        user.setBirthDate(this.birthDate);
        user.setLocation(this.location);

        return user;
    }
}
