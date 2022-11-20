package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.UserProfileOutput;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/user")
public class UsersController {

    private UserRepository users;

    @Autowired
    public UsersController(UserRepository users) {
        this.users = users;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileOutput> getUserProfileInfo(@PathVariable("id") Long userId) {
        Optional<User> possibleUser = users.findById(userId);

        if (!possibleUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UserProfileOutput output = new UserProfileOutput(possibleUser.get());
        return ResponseEntity.ok(output);
    }

}
