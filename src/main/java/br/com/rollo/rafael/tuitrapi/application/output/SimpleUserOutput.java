package br.com.rollo.rafael.tuitrapi.application.output;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleUserOutput {
    private Long id;
    private String username;
    private String fullName;

    private SimpleUserOutput(User user) {
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

    public static SimpleUserOutput buildFrom(User user) {
        return new SimpleUserOutput(user);
    }

    public static List<SimpleUserOutput> listFrom(List<User> users) {
        return users.stream()
                .map(SimpleUserOutput::buildFrom)
                .collect(Collectors.toList());
    }
}
