package br.com.rollo.rafael.tuitrapi.security;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

	@Value("${cdc.security.jwt.secret}")
	private String secret;
	
	@Value("${cdc.security.jwt.expiration}")
	private long expirationInMillis;
	
	public String generateToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
				
		final Date now = new Date();
        final Date expiration = new Date(now.getTime() + 
                this.expirationInMillis);

		return Jwts.builder()
			.setIssuer("Tuitr API")
			.setSubject(user.getUsername())
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(SignatureAlgorithm.HS256, this.secret)
			.compact();
	}
	
    public boolean isValid(String jwt) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String jwt) {
        return Jwts.parser().setSigningKey(this.secret)
            .parseClaimsJws(jwt).getBody().getSubject();
    }

}
