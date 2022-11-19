package br.com.rollo.rafael.tuitrapi.domain.users;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = -765987350472317177L;
	
	public static final Role ADMIN = new Role("ROLE_ADMIN");
    public static final Role CLIENT = new Role("ROLE_CLIENT");
	
	@Id
	private String authority;

	/**
     * @deprecated
     */
	public Role() {
	}

	public Role(String authority) {
		this.authority = authority;
	}
	
	@Override
	public String getAuthority() {
		return this.authority;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(authority, role.authority);
    }

}
