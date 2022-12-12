package br.com.rollo.rafael.tuitrapi.domain.posts;

import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface PostRepository extends Repository<Post, Long>, CustomizedPostRepository {
    Post save(Post post);
}
