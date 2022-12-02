package br.com.rollo.rafael.tuitrapi.domain.followers;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowerRemoval {

    private UserRepository users;

    @Autowired
    public FollowerRemoval(UserRepository users) {
        this.users = users;
    }

    public void execute(User user, User follower) {
        User managedUser = users.findById(user.getId()).get();

        managedUser.removeFollower(follower);
        follower.removeFollowing(managedUser);
    }
}
