package br.com.rollo.rafael.tuitrapi.domain.posts;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostsBuilder {

    private final List<Post> posts = new ArrayList<>();

    public static PostsBuilder asList() {
        return new PostsBuilder();
    }

    @SuppressWarnings("deprecation")
    public PostsBuilder post(String at, String content, User author) {
        var post = Post.createAsSinglePost(Post.Content.withText(content), author);
        post.setCreatedAt(LocalDateTime.parse(at));

        this.posts.add(post);
        return this;
    }

    @SuppressWarnings("deprecation")
    public PostsBuilder repost(String at, User author, Integer postAtIndex) {
        var post = Post.createAsRepost(author, this.posts.get(postAtIndex));
        post.setCreatedAt(LocalDateTime.parse(at));

        this.posts.add(post);
        return this;
    }

    public PostsBuilder repost(String at, String content, User author, Integer postAtIndex) {
        return repost(at, author, postAtIndex);
    }

    public List<Post> build() {
        return this.posts;
    }

    public List<Post> buildReversed() {
        List<Post> listCopy = new ArrayList<>(this.posts);
        Collections.reverse(listCopy);

        return listCopy;
    }

}
