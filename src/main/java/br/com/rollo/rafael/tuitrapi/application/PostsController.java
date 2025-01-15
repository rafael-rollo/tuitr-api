package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.input.PostInput;
import br.com.rollo.rafael.tuitrapi.application.output.PostOutput;
import br.com.rollo.rafael.tuitrapi.application.validators.AtLeastOneInformativeContentValidator;
import br.com.rollo.rafael.tuitrapi.application.validators.BindingException;
import br.com.rollo.rafael.tuitrapi.domain.posts.Post;
import br.com.rollo.rafael.tuitrapi.domain.posts.PostRepository;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import br.com.rollo.rafael.tuitrapi.infrastructure.security.ACLPermissionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostsController {

    private final PostRepository posts;
    private final UserRepository users;

    private final ACLPermissionRecord<Post> postsPermissionRecord;

    @Autowired
    public PostsController(PostRepository posts, UserRepository users, ACLPermissionRecord<Post> postsPermissionRecord) {
        this.posts = posts;
        this.users = users;
        this.postsPermissionRecord = postsPermissionRecord;
    }

    @InitBinder("postInput")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new AtLeastOneInformativeContentValidator());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostOutput>> listPosts(@AuthenticationPrincipal User loggedUser) {
        var posts = this.posts.findAllPostsOf(loggedUser);
        return ResponseEntity.ok(PostOutput.listFrom(posts));
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostOutput> createPost(@RequestBody @Valid PostInput postInput,
                                                 @AuthenticationPrincipal User loggedUser,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder URIBuilder) {
        if (bindingResult.hasErrors()) {
            throw new BindingException(bindingResult);
        }

        User author = users.findByUsername(loggedUser.getUsername()).get();

        var savedPost = posts.save(postInput.buildFor(author));
        postsPermissionRecord.executeFor(savedPost, BasePermission.ADMINISTRATION);

        var postPath = URIBuilder
                .path("/api/post/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(postPath)
                .body(PostOutput.buildFrom(savedPost));
    }
}
