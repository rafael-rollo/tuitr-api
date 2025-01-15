package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.SimpleUserOutput;
import br.com.rollo.rafael.tuitrapi.domain.follows.FollowerRemoval;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/follower")
public class FollowersController {

    private UserRepository users;
    private FollowerRemoval followerRemoval;

    @Autowired
    public FollowersController(UserRepository users, FollowerRemoval followerRemoval) {
        this.users = users;
        this.followerRemoval = followerRemoval;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleUserOutput>> listFollowers(@AuthenticationPrincipal User loggedUser) {
        var followers = users.findAllFollowersBy(loggedUser.getId());
        return ResponseEntity.ok(SimpleUserOutput.listFrom(followers));
    }

    @Transactional
    @DeleteMapping(value = "/{followerId}")
    public ResponseEntity<Void> removeFollower(@PathVariable Long followerId,
                                               @AuthenticationPrincipal User loggedUser) {

        Optional<User> follower = users.findById(followerId);

        if (follower.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        followerRemoval.execute(loggedUser, follower.get());
        return ResponseEntity.noContent().build();
    }
}
