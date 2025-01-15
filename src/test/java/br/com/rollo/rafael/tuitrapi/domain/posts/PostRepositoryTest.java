package br.com.rollo.rafael.tuitrapi.domain.posts;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    User user = new User("rollo");

    List<Post> expectedFeedPosts = PostsBuilder.asList()
            .post("2022-12-06T17:34:48", "Welcome to the Tuítr", new User("tuitr"))
            .post("2025-01-05T09:00:00", "The unexamined life is not worth living.", new User("socrates"))
            .post("2025-01-05T09:01:00", "My teacher @socrates once told me...", new User("plato"))
            .post("2025-01-05T09:15:00", "All of your arguments are rooted in...", new User("descartes"))
            .post("2025-01-05T09:20:00", "History is a dialectical process...", new User("hegel"))
            .repost("2025-01-05T09:30:00", new User("rollo"), 1)
            .post("2025-01-05T09:40:00", "Government exists to protect our...", new User("johnlocke"))
            .repost("2025-01-05T09:42:00", "The foundation of moral law...", new User("immanuelkant"), 6)
            .post("2025-01-06T09:44:00", "Ethics must be derived from...", new User("spinoza"))
            .post("2025-01-08T09:50:00", "We cannot trust our senses...", new User("descartes"))
            .buildReversed();

    @BeforeAll
    static void setup() {
        // do-nothing
        // flyway migration will run once as the spring test set up the context
    }

    @Test
    void shouldQueryFeedPostsBasedOnInitMigrationDataCorrectly() {
        Iterable<Post> feed = postRepository.findAllPostsOfFollowedAccountsBy(user);

        // fetch exactly 10 posts for user rollo
        assertThat(feed).hasSize(10);

        // doesn't fetch replies, just posts and reposts
        assertThat(feed).allMatch(post -> !post.isReply());

        // assert the exact posts
        assertThat(feed).isEqualTo(expectedFeedPosts);
    }

}
