package br.com.rollo.rafael.tuitrapi.domain.follows;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowAdding {

    private UserRepository users;

    @Autowired
    public FollowAdding(UserRepository users) {
        this.users = users;
    }

    public User execute(User user, User followingUser) {
        User managedUser = users.findById(user.getId()).get();

        managedUser.follow(followingUser);
        return followingUser;
    }
}
