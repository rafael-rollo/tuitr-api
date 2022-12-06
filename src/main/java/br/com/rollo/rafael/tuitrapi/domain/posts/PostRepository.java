package br.com.rollo.rafael.tuitrapi.domain.posts;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@org.springframework.stereotype.Repository
public interface PostRepository extends Repository<Post, Long> {

    List<Post> findAllByAuthorUsername(String username);

    Post save(Post post);

    @Query("select p from Post p where p.author.id in (select following.id from User u join u.following following where u.id = :userId)")
    List<Post> findPostsOfFollowedAccountsBy(@Param("userId") Long userId);
}
