package br.com.rollo.rafael.tuitrapi.security;

import br.com.rollo.rafael.tuitrapi.domain.users.UserLoading;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
	
    private TokenManager tokenManager;
    private UserLoading userLoading;

    public JWTAuthenticationFilter(TokenManager tokenManager, UserLoading userLoading) {
	    this.tokenManager = tokenManager;
	    this.userLoading = userLoading;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = getTokenFrom(request);
		
        if (tokenManager.isValid(jwt)) {
            String username = tokenManager.getUsernameFromToken(jwt);
            UserDetails userDetails = userLoading.loadUserByUsername(username);
            
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, 
                        null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

		
		filterChain.doFilter(request, response);
	}

	private String getTokenFrom(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		
		return null;
	}
	
	
}
