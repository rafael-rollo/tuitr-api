package br.com.rollo.rafael.tuitrapi.domain.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUpdate {

    private final UserRepository users;

    @Autowired
    public UserUpdate(UserRepository users) {
        this.users = users;
    }

    public User executeFor(String username, User userUpdate) {
        var foundUser = users.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No user found for " + username));

        foundUser.updateBy(userUpdate);
        return foundUser;
    }

}
