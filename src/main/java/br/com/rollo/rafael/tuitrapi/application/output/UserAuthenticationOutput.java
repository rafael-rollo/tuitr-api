package br.com.rollo.rafael.tuitrapi.application.output;

import br.com.rollo.rafael.tuitrapi.domain.users.User;

public class UserAuthenticationOutput {
	
    private String username;
    private String role;
    private Authentication authentication;
    
    public UserAuthenticationOutput(User user, String token) {
        this.username = user.getUsername();
        this.role = user.getPrimaryRoleName();
        this.authentication = new Authentication("Bearer", token);
    }

	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role.split("_")[1];
	}

	public Authentication getAuthentication() {
		return authentication;
	}
	
	public static class Authentication {
		private String tokenType;
	    private String token;

	    public Authentication(String tokenType, String token) {
	        this.tokenType = tokenType;
	        this.token = token;
	    }

	    public String getToken() {
	        return token;
	    }

	    public String getTokenType() {
	        return tokenType;
	    }
	}

}
