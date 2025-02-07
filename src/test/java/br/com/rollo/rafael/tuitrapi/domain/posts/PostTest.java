package br.com.rollo.rafael.tuitrapi.domain.posts;

import static br.com.rollo.rafael.tuitrapi.domain.posts.Post.Content;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    private final User originPostAuthor = new User(
                "john-doe",
                        "john.doe@dmn.co",
                        "***");

    private final Post originPost = Post.createAsSinglePost(
            Content.withText("Hi everyone!"),
            originPostAuthor);

    @Test
    public void shouldCreatedASinglePostProperly() {
        User author = new User(
                "tuitr",
                "admin@tuitr.com",
                "***");

        Post createdAsSingle = Post.createAsSinglePost(
                Content.withText("Welcome to the Tuítr"),
                author);

        assertThat(createdAsSingle.isReply()).isFalse();
        assertThat(createdAsSingle.isRepost()).isFalse();
    }

    @Test
    public void shouldCreateAReplyPostProperly() {
        User author = new User(
                "tuitr",
                "admin@tuitr.com",
                "***");

        Post createdAsReply = Post.createAsReply(
                Content.withText("Hi John, welcome to the Tuítr"),
                author,
                originPost);

        assertThat(createdAsReply.isReply()).isTrue();
        assertThat(createdAsReply.getReplyingTo()).isEqualTo(originPost);

        assertThat(createdAsReply.isRepost()).isFalse();
        assertThat(createdAsReply.getReposting()).isEqualTo(null);
    }

    @Test
    public void shouldCreateARepostProperly() {
        User author = new User(
                "tuitr",
                "admin@tuitr.com",
                "***");

        Post createdAsRepost = Post.createAsRepost(
                author,
                originPost);

        assertThat(createdAsRepost.isReply()).isFalse();
        assertThat(createdAsRepost.getReplyingTo()).isEqualTo(null);

        assertThat(createdAsRepost.isRepost()).isTrue();
        assertThat(createdAsRepost.getReposting()).isEqualTo(originPost);
    }

}
