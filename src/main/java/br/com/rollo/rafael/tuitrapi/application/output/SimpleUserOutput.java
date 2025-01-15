package br.com.rollo.rafael.tuitrapi.application.output;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleUserOutput {
    private final Long id;
    private final String username;
    private final String fullName;
    private final String profilePicturePath;

    private SimpleUserOutput(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.profilePicturePath = user.getProfilePicturePath();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public static SimpleUserOutput buildFrom(User user) {
        return new SimpleUserOutput(user);
    }

    public static List<SimpleUserOutput> listFrom(List<User> users) {
        return users.stream()
                .map(SimpleUserOutput::buildFrom)
                .collect(Collectors.toList());
    }
}
