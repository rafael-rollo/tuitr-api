package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.input.SignUpInput;
import br.com.rollo.rafael.tuitrapi.application.input.UpdateProfileInput;
import br.com.rollo.rafael.tuitrapi.application.output.UserProfileOutput;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import br.com.rollo.rafael.tuitrapi.domain.users.UserUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UsersController {

    private final UserRepository users;
    private final UserUpdate userUpdate;

    @Autowired
    public UsersController(UserRepository users, UserUpdate userUpdate) {
        this.users = users;
        this.userUpdate = userUpdate;
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileOutput> getUserProfileInfo(@PathVariable String username) {
        Optional<User> user = users.findByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(UserProfileOutput.buildFrom(user.get()));
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> userSignUp(@Valid @RequestBody SignUpInput input,
                                           BCryptPasswordEncoder encoder,
                                           UriComponentsBuilder URIBuilder) {
        var newUser = users.save(input.build(encoder));

        var userPath = URIBuilder
                .path("/api/user/{username}")
                .buildAndExpand(newUser.getUsername())
                .toUri();

        return ResponseEntity.created(userPath).build();
    }

    @Transactional
    @PutMapping(value = "/{username}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileOutput> updateUserProfile(@PathVariable String username,
                                                               @Valid @RequestBody UpdateProfileInput input,
                                                               @AuthenticationPrincipal User loggedUser) {
        boolean accessGranted = loggedUser.isUserOf(username) || loggedUser.isAdmin();
        if (!accessGranted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var user = input.build();

        var updatedUser = userUpdate.executeFor(username, user);
        return ResponseEntity.ok(UserProfileOutput.buildFrom(updatedUser));
    }

    @Transactional
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable String username,
                                                  @AuthenticationPrincipal User loggedUser) {
        boolean accessGranted = loggedUser.isUserOf(username) || loggedUser.isAdmin();
        if (!accessGranted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        users.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
