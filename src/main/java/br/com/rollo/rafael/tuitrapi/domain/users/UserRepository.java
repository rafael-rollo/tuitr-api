package br.com.rollo.rafael.tuitrapi.domain.users;

import org.springframework.data.repository.Repository;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User, Long>{

	Optional<User> findByUsername(String username);

	Optional<User> findById(Long id);
}
