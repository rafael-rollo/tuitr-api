package br.com.rollo.rafael.tuitrapi.domain.follows;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowRemoval {

    private static final String BROADCAST_USER_USERNAME = "tuitr";

    private UserRepository users;

    @Autowired
    public FollowRemoval(UserRepository users) {
        this.users = users;
    }

    public void execute(User user, User unfollowingUser) {
        if (BROADCAST_USER_USERNAME.equals(unfollowingUser.getUsername())) {
            throw new BroadcastUserUnfollowException("It is not possible to unfollow the base user of the platform");
        }

        User managedUser = users.findById(user.getId()).get();
        managedUser.unfollow(unfollowingUser);
    }
}
