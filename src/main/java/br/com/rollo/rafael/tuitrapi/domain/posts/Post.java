package br.com.rollo.rafael.tuitrapi.domain.posts;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Length(max = 280)
    private String textContent;
    private String imagePath;

    @ManyToOne
    private User author;

    @ManyToOne
    private Post replyingTo;

    @OneToMany(mappedBy = "replyingTo")
    private final List<Post> replies = new ArrayList<>();

    @ManyToOne
    private Post reposting;

    @OneToMany(mappedBy = "reposting")
    private final List<Post> reposts = new ArrayList<>();

    @ManyToMany
    private final Set<User> lovers = new HashSet<>();

    /**
     * @deprecated in favor of createAsSinglePost(content, author) |
     * createAsReply(content, author, replyingTo Post) |
     * createAsRepost(author, reposting Post)
     */
    @Deprecated
    public Post() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @deprecated in favor of using the default one from Post() constructor
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public void setCreatedAt(LocalDateTime creationDateTime) {
        this.createdAt = creationDateTime;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getImagePath() {
        return imagePath;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Post getReplyingTo() {
        return replyingTo;
    }

    public List<Post> getReplies() {
        return replies;
    }

    public boolean isReply() {
        return replyingTo != null;
    }

    public Post getReposting() {
        return reposting;
    }

    public List<Post> getReposts() {
        return reposts;
    }

    public boolean isRepost() {
        return reposting != null;
    }

    public Set<User> getLovers() {
        return Collections.unmodifiableSet(this.lovers);
    }

    public void addLover(User lover) {
        this.lovers.add(lover);
    }

    public void removeLover(User lover) {
        this.lovers.remove(lover);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var post = (Post) o;
        return Objects.equals(createdAt, post.createdAt) && Objects.equals(author, post.author) && Objects.equals(replyingTo, post.replyingTo) && Objects.equals(reposting, post.reposting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, author, replyingTo, reposting);
    }

    @Override
    public String toString() {
        return "Post { createdBy= '" + this.author.getUsername() + "', " +
                "at= '" + this.createdAt + "', " +
                "content= '" + this.textContent + "', " +
                "withImage= '" + this.imagePath + "'" +
                ( this.isReply() ? ", replyingTo= 'Post " + this.replyingTo.getId() + "' " : "" ) +
                ( this.isRepost() ? ", reposting= 'Post " + this.reposting.getId() + "'" : "" ) +
                " }";
    }

    private void setContent(Content postContent) {
        this.textContent = postContent.getText();
        this.imagePath = postContent.getImagePath();
    }

    public static Post createAsSinglePost(Content content, User author) {
        Post post = new Post();
        post.replyingTo = null;
        post.reposting = null;

        post.setContent(content);
        post.setAuthor(author);

        return post;
    }

    public static Post createAsReply(Content content, User author, Post replyingTo) {
        Post post = new Post();
        post.replyingTo = replyingTo;
        post.reposting = null;

        post.setContent(content);
        post.setAuthor(author);

        return post;
    }

    public static Post createAsRepost(User author, Post reposting) {
        Post post = new Post();
        post.replyingTo = null;
        post.reposting = reposting;

        post.setAuthor(author);
        return post;
    }

    public static class Content {
        private final String text;
        private final String imagePath;

        private Content(String textContent, String imagePath) {
            this.text = textContent;
            this.imagePath = imagePath;
        }

        public static Content withText(String text) {
            return new Content(text, null);
        }

        public static Content withImage(String imagePath) {
            return new Content(null, imagePath);
        }

        public static Content withTextAndImage(String text, String imagePath) {
            return new Content(text, imagePath);
        }

        public String getText() {
            return text;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}
