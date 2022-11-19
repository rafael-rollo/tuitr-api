package br.com.rollo.rafael.tuitrapi.application;

import br.com.rollo.rafael.tuitrapi.application.input.SignInInput;
import br.com.rollo.rafael.tuitrapi.application.output.UserAuthenticationOutput;
import br.com.rollo.rafael.tuitrapi.domain.users.User;
import br.com.rollo.rafael.tuitrapi.domain.users.UserRepository;
import br.com.rollo.rafael.tuitrapi.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenManager tokenGeneration;

	@Autowired
	private UserRepository userRepository;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserAuthenticationOutput> authenticate(@RequestBody SignInInput signInInfo) {
		UsernamePasswordAuthenticationToken authenticationToken = signInInfo.build();

		try {
			Authentication authentication = authManager
					.authenticate(authenticationToken);

			String jwt = tokenGeneration.generateToken(authentication);
			UserAuthenticationOutput tokenOutput =
					new UserAuthenticationOutput((User) authentication.getPrincipal(), jwt);

			return ResponseEntity.ok(tokenOutput);

		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}

}
