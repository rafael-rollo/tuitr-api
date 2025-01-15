package br.com.rollo.rafael.tuitrapi.infrastructure.configuration;

import br.com.rollo.rafael.tuitrapi.domain.users.UserLoading;
import br.com.rollo.rafael.tuitrapi.infrastructure.security.JWTAuthenticationFilter;
import br.com.rollo.rafael.tuitrapi.infrastructure.security.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private static final String[] AUTH_WHITELIST = {
		// springdoc-swagger
		"/v3/api-docs/**",
		// other public endpoints
		"/error/**",
		"/favicon.**",
		"/actuator/**",
		"/h2-console/**"
	};

	@Autowired
	private TokenManager tokenManager;
	
	@Autowired
	private UserLoading userLoading;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder encoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(encoder);
		return new ProviderManager(provider);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		var jwtAuthenticationFilter = new JWTAuthenticationFilter(tokenManager, userLoading);

		http
				.authorizeHttpRequests((auth) -> {
					auth.requestMatchers("/api/auth").permitAll()
							.requestMatchers(HttpMethod.POST, "/api/user").permitAll()
							.requestMatchers(HttpMethod.GET, "/api/user/**").permitAll()
							.requestMatchers(AUTH_WHITELIST).permitAll()
							.anyRequest().authenticated();
				})
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement((session) -> {
					session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.addFilterBefore(new JWTAuthenticationFilter(tokenManager, userLoading),
						/*before*/ UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(config -> {
					config.authenticationEntryPoint(new JWTAuthenticationEntryPoint());
				});

		return http.build();
	}
    
    private static class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    	private static final Logger logger = LoggerFactory
                .getLogger(JWTAuthenticationEntryPoint.class);
    	
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			
			logger.error("Um acesso não autorizado à {} foi verificado. {}",
					request.getRequestURI(),
                    authException.getMessage());

			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, 
                    "Não é possível acessar esse recurso.");

		}
    }
}
