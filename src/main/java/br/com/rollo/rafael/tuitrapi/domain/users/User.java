package br.com.rollo.rafael.tuitrapi.domain.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class User implements UserDetails, UpdatableUserInfo {
	
	private static final long serialVersionUID = -5469393828158054187L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    @ManyToMany(fetch=FetchType.EAGER)
    private List<Role> authorities = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String email;
    private String fullName;
    private LocalDate birthDate;
    private LocalDate joinedAt;
    private String location;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<User> followers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<User> following = new ArrayList<>();

    /**
        * @deprecated
        */
    public User() {	}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.joinedAt = LocalDate.now();
    }

    public User(String username) {
        this.username = username;
        this.joinedAt = LocalDate.now();
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                ", fullName='" + fullName + '\'' +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", joinedAt=" + joinedAt +
                '}';
    }

    public String getPrimaryRoleName() {
		return this.authorities.get(0).getAuthority();
	}

    public void updateBy(UpdatableUserInfo updateInfo) {
        this.username = updateInfo.getUsername();
        this.fullName = updateInfo.getFullName();
        this.birthDate = updateInfo.getBirthDate();
        this.location = updateInfo.getLocation();
    }
}

