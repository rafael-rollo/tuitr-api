package br.com.rollo.rafael.tuitrapi.domain.posts;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

import java.util.List;

public interface CustomizedPostRepository {
    List<Post> findAllPostsOf(User user);
    List<Post> findAllPostsOfFollowedAccountsBy(User user);
}

