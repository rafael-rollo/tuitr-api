package br.com.rollo.rafael.tuitrapi.infrastructure.security;

import br.com.rollo.rafael.tuitrapi.domain.users.UserLoading;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
    private final TokenManager tokenManager;
    private final UserLoading userLoading;

    public JWTAuthenticationFilter(TokenManager tokenManager, UserLoading userLoading) {
	    this.tokenManager = tokenManager;
	    this.userLoading = userLoading;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		var jwt = getTokenFrom(request);
		
        if (tokenManager.isValid(jwt)) {
            var username = tokenManager.getUsernameFromToken(jwt);
            var userDetails = userLoading.loadUserByUsername(username);
            
            var authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

		
		filterChain.doFilter(request, response);
	}

	private String getTokenFrom(HttpServletRequest request) {
		var bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
	
	
}
