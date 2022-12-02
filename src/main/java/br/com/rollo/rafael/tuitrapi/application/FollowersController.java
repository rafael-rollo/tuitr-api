package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.FollowerOutput;
import br.com.rollo.rafael.tuitrapi.domain.followers.FollowerRemoval;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/user/follower")
public class FollowersController {

    private UserRepository users;
    private FollowerRemoval followerRemoval;

    @Autowired
    public FollowersController(UserRepository users, FollowerRemoval followerRemoval) {
        this.users = users;
        this.followerRemoval = followerRemoval;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FollowerOutput>> listFollowers(@AuthenticationPrincipal User loggedUser) {
        List<User> followers = users.findAllFollowersBy(loggedUser.getId());
        return ResponseEntity.ok(FollowerOutput.listFrom(followers));
    }

    @Transactional
    @RequestMapping(value = "/{followerId}")
    public ResponseEntity<Void> removeFollower(@PathVariable Long followerId,
                                               @AuthenticationPrincipal User loggedUser) {

        Optional<User> follower = users.findById(followerId);

        if (!follower.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        followerRemoval.execute(loggedUser, follower.get());
        return ResponseEntity.noContent().build();
    }
}
