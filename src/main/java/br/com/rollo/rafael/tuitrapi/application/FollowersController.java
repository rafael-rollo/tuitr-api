package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.FollowerOutput;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/user/follower")
public class FollowersController {

    private UserRepository users;

    @Autowired
    public FollowersController(UserRepository users) {
        this.users = users;
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FollowerOutput>> listFollowers(@AuthenticationPrincipal User loggedUser) {
        List<User> followers = users.findAllFollowersBy(loggedUser.getId());
        return ResponseEntity.ok(FollowerOutput.listFrom(followers));
    }
}
