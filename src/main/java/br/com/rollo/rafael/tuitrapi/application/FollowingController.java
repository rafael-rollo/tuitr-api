package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.SimpleUserOutput;
import br.com.rollo.rafael.tuitrapi.domain.follows.FollowAdding;
import br.com.rollo.rafael.tuitrapi.domain.follows.FollowRemoval;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/following")
public class FollowingController {

    private final UserRepository users;
    private final FollowAdding followAdding;
    private final FollowRemoval followRemoval;

    @Autowired
    public FollowingController(UserRepository users, FollowAdding followAdding, FollowRemoval followRemoval) {
        this.users = users;
        this.followAdding = followAdding;
        this.followRemoval = followRemoval;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleUserOutput>> listFollowing(@AuthenticationPrincipal User loggedUser) {
        var followingUsers = users.findAllFollowingsBy(loggedUser.getId());
        return ResponseEntity.ok(SimpleUserOutput.listFrom(followingUsers));
    }

    @Transactional
    @PostMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleUserOutput> followUser(@PathVariable Long userId,
                                                       @AuthenticationPrincipal User loggedUser) {
        Optional<User> following = users.findById(userId);

        if (following.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User followed = followAdding.execute(loggedUser, following.get());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SimpleUserOutput.buildFrom(followed));
    }

    @Transactional
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userId,
                                             @AuthenticationPrincipal User loggedUser) {
        Optional<User> unfollowing = users.findById(userId);

        if (unfollowing.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        followRemoval.execute(loggedUser, unfollowing.get());
        return ResponseEntity.noContent().build();
    }

}
