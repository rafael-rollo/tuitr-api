package br.com.rollo.rafael.tuitrapi.application.output;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class FollowerOutput {
    private Long id;
    private String username;
    private String fullName;

    private FollowerOutput(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
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

    public static FollowerOutput buildFrom(User user) {
        return new FollowerOutput(user);
    }

    public static List<FollowerOutput> listFrom(List<User> users) {
        return users.stream()
                .map(FollowerOutput::buildFrom)
                .collect(Collectors.toList());
    }
}
