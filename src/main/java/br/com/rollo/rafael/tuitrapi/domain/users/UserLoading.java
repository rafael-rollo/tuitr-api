package br.com.rollo.rafael.tuitrapi.domain.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserLoading implements UserDetailsService {

	@Autowired
	private UserRepository users;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = users.findByUsername(username);
		
		return user.orElseThrow(() ->
			new UsernameNotFoundException("Não foi encontrado usuário para o username: " + username));
	}
}
