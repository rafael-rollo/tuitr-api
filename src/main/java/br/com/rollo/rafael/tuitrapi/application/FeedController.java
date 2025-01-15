package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.PostOutput;
import br.com.rollo.rafael.tuitrapi.domain.posts.Post;
import br.com.rollo.rafael.tuitrapi.domain.posts.PostRepository;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static br.com.rollo.rafael.tuitrapi.infrastructure.configuration.SwaggerConfiguration.SECURITY_SCHEME_KEY;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final PostRepository posts;

    @Autowired
    public FeedController(PostRepository posts) {
        this.posts = posts;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(security = { @SecurityRequirement(name = SECURITY_SCHEME_KEY) })
    public ResponseEntity<List<PostOutput>> loadFeed(@AuthenticationPrincipal User loggedUser) {
        var posts = this.posts.findAllPostsOfFollowedAccountsBy(loggedUser);
        return ResponseEntity.ok(PostOutput.listFrom(posts));
    }

}
