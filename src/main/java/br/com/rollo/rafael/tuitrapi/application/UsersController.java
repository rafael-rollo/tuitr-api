package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.input.SignUpInput;
import br.com.rollo.rafael.tuitrapi.application.output.UserProfileOutput;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@Controller
@RequestMapping("/api/user")
public class UsersController {

    private UserRepository users;

    @Autowired
    public UsersController(UserRepository users) {
        this.users = users;
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileOutput> getUserProfileInfo(@PathVariable String username) {
        Optional<User> possibleUser = users.findByUsername(username);

        if (!possibleUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        UserProfileOutput output = new UserProfileOutput(possibleUser.get());
        return ResponseEntity.ok(output);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> userSignUp(@Valid @RequestBody SignUpInput input,
                                           BCryptPasswordEncoder encoder,
                                           UriComponentsBuilder URIBuilder) {
        User newUser = users.save(input.build(encoder));

        URI userPath = URIBuilder
                .path("/api/user/{username}")
                .buildAndExpand(newUser.getUsername())
                .toUri();

        return ResponseEntity.created(userPath).build();
    }

}
