package br.com.rollo.rafael.tuitrapi.domain.follows;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerRemoval {

    private final UserRepository users;

    @Autowired
    public FollowerRemoval(UserRepository users) {
        this.users = users;
    }

    public void execute(User user, User follower) {
        var managedUser = users.findById(user.getId()).get();
        managedUser.removeFollower(follower);
    }
}
