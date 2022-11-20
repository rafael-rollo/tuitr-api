package br.com.rollo.rafael.tuitrapi.application.input;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SignInInput {
	
	private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsernamePasswordAuthenticationToken build() {
        return new UsernamePasswordAuthenticationToken(this.username, this.password);
    }
}
