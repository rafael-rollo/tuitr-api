package br.com.rollo.rafael.tuitrapi.application.output;

import br.com.rollo.rafael.tuitrapi.domain.posts.Post;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostOutput {

    private final Long id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;
    private final String textContent;
    private final String imagePath;
    private final SimpleUserOutput author;
    private final PostOutput replyingTo;
    private final PostOutput reposting;
    private final int loves;
    private final int replies;
    private final int reposts;

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
        this.replies = post.getReplies().size();
        this.reposts = post.getReposts().size();
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

    public int getReplies() {
        return replies;
    }

    public int getReposts() {
        return reposts;
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
