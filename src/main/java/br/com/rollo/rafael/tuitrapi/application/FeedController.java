package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.output.PostOutput;
import br.com.rollo.rafael.tuitrapi.domain.posts.Post;
import br.com.rollo.rafael.tuitrapi.domain.posts.PostRepository;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/feed")
public class FeedController {

    private PostRepository posts;

    @Autowired
    public FeedController(PostRepository posts) {
        this.posts = posts;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostOutput>> loadFeed(@AuthenticationPrincipal User loggedUser) {
        List<Post> posts = this.posts.findPostsOfFollowedAccountsBy(loggedUser.getId());
        return ResponseEntity.ok(PostOutput.listFrom(posts));
    }

}
