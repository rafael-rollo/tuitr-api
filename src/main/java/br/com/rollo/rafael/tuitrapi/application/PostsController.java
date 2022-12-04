package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.input.PostInput;
import br.com.rollo.rafael.tuitrapi.application.output.PostOutput;
import br.com.rollo.rafael.tuitrapi.application.validators.AtLeastOneInformativeContentValidator;
import br.com.rollo.rafael.tuitrapi.application.validators.BindingException;
import br.com.rollo.rafael.tuitrapi.domain.posts.Post;
import br.com.rollo.rafael.tuitrapi.domain.posts.PostRepository;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import br.com.rollo.rafael.tuitrapi.security.ACLPermissionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/post")
public class PostsController {

    private PostRepository posts;
    private UserRepository users;

    private ACLPermissionRecord<Post> postsPermissionRecord;

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
        List<Post> posts = this.posts.findAllByAuthorUsername(loggedUser.getUsername());
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

        Post savedPost = posts.save(postInput.buildFor(author));
        postsPermissionRecord.executeFor(savedPost, BasePermission.ADMINISTRATION);

        URI postPath = URIBuilder
                .path("/api/post/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(postPath)
                .body(PostOutput.buildFrom(savedPost));
    }
}
