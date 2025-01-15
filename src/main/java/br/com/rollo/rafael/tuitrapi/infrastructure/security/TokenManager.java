package br.com.rollo.rafael.tuitrapi.infrastructure.security;

import br.com.rollo.rafael.tuitrapi.domain.users.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenManager {

	@Value("${cdc.security.jwt.secret}")
	private String secret;
	
	@Value("${cdc.security.jwt.expiration}")
	private long expirationInMillis;

	private static SecretKey keyFrom(String secret) {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	private Jws<Claims> parse(String jwt) {
		var secretKey = keyFrom(this.secret);
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt);
	}
	
	public String generateToken(Authentication authentication) {
		var user = (User) authentication.getPrincipal();
				
		final Date now = new Date();
        final Date expiration = new Date(now.getTime() + 
                this.expirationInMillis);

		return Jwts.builder()
				.issuer("Tuitr API")
				.subject(user.getUsername())
				.issuedAt(now)
				.expiration(expiration)
				.signWith(keyFrom(this.secret))
				.compact();
	}
	
    public boolean isValid(String jwt) {
        try {
			parse(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String jwt) {
        return parse(jwt).getPayload().getSubject();
    }

}
