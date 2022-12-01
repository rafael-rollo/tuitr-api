package br.com.rollo.rafael.tuitrapi.domain.users;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, Long>{

	Optional<User> findByUsername(String username);

	Optional<User> findById(Long id);

	User save(User user);

    Optional<User> findByEmail(String email);

    void deleteByUsername(String username);

    @Query("select u from User u where u.id in (select follower.id from User u join u.followers follower where u.id = :userId)")
    List<User> findAllFollowersBy(@Param("userId") Long id);

    @Query("select u from User u where u.id in (select following.id from User u join u.following following where u.id = :userId)")
    List<User> findAllFollowingsBy(@Param("userId") Long id);
}
