package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.FollowerOutput;
import br.com.rollo.rafael.tuitrapi.domain.follows.FollowAdding;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/user/following")
public class FollowingController {

    private UserRepository users;
    private FollowAdding followAdding;

    @Autowired
    public FollowingController(UserRepository users, FollowAdding followAdding) {
        this.users = users;
        this.followAdding = followAdding;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FollowerOutput>> listFollowing(@AuthenticationPrincipal User loggedUser) {
        List<User> followingUsers = users.findAllFollowingsBy(loggedUser.getId());
        return ResponseEntity.ok(FollowerOutput.listFrom(followingUsers));
    }

    @Transactional
    @PostMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FollowerOutput> followUser(@PathVariable Long userId,
                                                     @AuthenticationPrincipal User loggedUser) {

        Optional<User> following = users.findById(userId);

        if (!following.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User followed = followAdding.execute(loggedUser, following.get());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(FollowerOutput.buildFrom(followed));
    }

}
