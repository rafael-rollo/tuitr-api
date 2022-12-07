package br.com.rollo.rafael.tuitrapi.application.output;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

import java.time.LocalDate;

public class UserProfileOutput {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String profilePicturePath;
    private LocalDate birthDate;
    private LocalDate joinedAt;
    private String location;

    private UserProfileOutput(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.profilePicturePath = user.getProfilePicturePath();
        this.birthDate = user.getBirthDate();
        this.joinedAt = user.getJoinedAt();
        this.location = user.getLocation();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }

    public String getLocation() {
        return location;
    }

    public static UserProfileOutput buildFrom(User user) {
        return new UserProfileOutput(user);
    }
}