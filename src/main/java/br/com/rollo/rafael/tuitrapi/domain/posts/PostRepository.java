package br.com.rollo.rafael.tuitrapi.domain.posts;

import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface PostRepository extends Repository<Post, Long> {

    List<Post> findAllByAuthorUsername(String username);

    Post save(Post post);
}
