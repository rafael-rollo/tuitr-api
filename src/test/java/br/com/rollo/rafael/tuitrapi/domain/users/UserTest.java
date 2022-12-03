package br.com.rollo.rafael.tuitrapi.domain.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private User user1;
    private User user2;
    private User user3;
    private User user4;

    @BeforeEach
    public void setup() {
        user1 = new User("user-1", "user.1@dmn.co", "***");
        user2 = new User("user-2", "user.2@dmn.co", "***");
        user3 = new User("user-3", "user.3@dmn.co", "***");
        user4 = new User("user-4", "user.4@dmn.co", "***");

        user1.follow(user4);
        user2.follow(user1);
        user3.follow(user2);
        user4.follow(user3);
    }

    @Test
    public void shouldUpdateFollowerFollowingSetsAccordinglyWhenAUserFollowsAnother() {
        // given the setup state

        // when
        user1.follow(user2);
        user3.follow(user4);

        // then

        // state after user1 follows user2
        assertThat(user1.getFollowing().size()).isEqualTo(2);
        assertThat(user1.getFollowing()).contains(user2);
        assertThat(user1.getFollowing()).contains(user4);

        assertThat(user2.getFollowers().size()).isEqualTo(2);
        assertThat(user2.getFollowers()).contains(user1);
        assertThat(user2.getFollowers()).contains(user3);

        // state after user3 follows user4
        assertThat(user3.getFollowing().size()).isEqualTo(2);
        assertThat(user3.getFollowing()).contains(user2);
        assertThat(user3.getFollowing()).contains(user4);

        assertThat(user4.getFollowers().size()).isEqualTo(2);
        assertThat(user4.getFollowers()).contains(user1);
        assertThat(user4.getFollowers()).contains(user3);
    }

    @Test
    public void shouldUpdateFollowerFollowingSetsAccordinglyWhenAUserUnfollowsAnother() {
        // given
        user1.follow(user2);
        user3.follow(user4);

        // when undone the original follows
        user1.unfollow(user4);
        user3.unfollow(user2);

        // then

        // state after user1 unfollows user4
        assertThat(user1.getFollowing().size()).isEqualTo(1);
        assertThat(user1.getFollowing()).doesNotContain(user4);
        assertThat(user1.getFollowing()).contains(user2);

        assertThat(user4.getFollowers().size()).isEqualTo(1);
        assertThat(user4.getFollowers()).contains(user3);
        assertThat(user4.getFollowers()).doesNotContain(user1);

        // state after user3 unfollows user2
        assertThat(user3.getFollowing().size()).isEqualTo(1);
        assertThat(user3.getFollowing()).doesNotContain(user2);
        assertThat(user3.getFollowing()).contains(user4);

        assertThat(user2.getFollowers().size()).isEqualTo(1);
        assertThat(user2.getFollowers()).contains(user1);
        assertThat(user2.getFollowers()).doesNotContain(user3);
    }

    @Test
    public void shouldUpdateFollowerFollowingSetsAccordinglyWhenAUserRemovesAFollower() {
        // given
        user1.follow(user2);
        user3.follow(user4);

        // when undone the original follows
        user2.removeFollower(user3);
        user4.removeFollower(user1);

        // then

        // state after the user2 removes user3
        assertThat(user2.getFollowers().size()).isEqualTo(1);
        assertThat(user2.getFollowers()).doesNotContain(user3);
        assertThat(user2.getFollowers()).contains(user1);

        assertThat(user3.getFollowing().size()).isEqualTo(1);
        assertThat(user3.getFollowing()).doesNotContain(user2);
        assertThat(user3.getFollowing()).contains(user4);

        // state after the user4 removes user1
        assertThat(user4.getFollowers().size()).isEqualTo(1);
        assertThat(user4.getFollowers()).doesNotContain(user1);
        assertThat(user4.getFollowers()).contains(user3);

        assertThat(user1.getFollowing().size()).isEqualTo(1);
        assertThat(user1.getFollowing()).doesNotContain(user4);
        assertThat(user1.getFollowing()).contains(user2);
    }
}
