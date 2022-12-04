package br.com.rollo.rafael.tuitrapi.application.output;

import br.com.rollo.rafael.tuitrapi.domain.posts.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostOutput {

    private Long id;
    private LocalDateTime createdAt;
    private String textContent;
    private String imagePath;
    private SimpleUserOutput author;
    private PostOutput replyingTo;
    private PostOutput reposting;
    private int loves;

    private PostOutput(Post post) {
        this.id = post.getId();
        this.createdAt = post.getCreatedAt();
        this.textContent = post.getTextContent();
        this.imagePath = post.getImagePath();
        this.author = SimpleUserOutput.buildFrom(post.getAuthor());
        this.replyingTo = post.isReply()
                ? new PostOutput(post.getReplyingTo())
                : null;
        this.reposting = post.isRepost()
                ? new PostOutput(post.getReposting())
                : null;
        this.loves = post.getLovers().size();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getImagePath() {
        return imagePath;
    }

    public SimpleUserOutput getAuthor() {
        return author;
    }

    public PostOutput getReplyingTo() {
        return replyingTo;
    }

    public PostOutput getReposting() {
        return reposting;
    }

    public int getLoves() {
        return loves;
    }

    public static PostOutput buildFrom(Post post) {
        return new PostOutput(post);
    }

    public static List<PostOutput> listFrom(List<Post> posts) {
        return posts.stream()
                .map(PostOutput::buildFrom)
                .collect(Collectors.toList());
    }

}
