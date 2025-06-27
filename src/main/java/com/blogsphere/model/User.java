package com.blogsphere.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	//Primary Key

	@Column(nullable = false)
	private String name;	//User's real name

	@Column(nullable = false, unique = true)
	private String username;	//User's unique name

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(name = "profile_picture_url", nullable = false)
	@Builder.Default
    private String profilePictureUrl = "default.jpg";;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Blog> blogs;
	
	@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followingUsers;

	@OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followers;
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of(new SimpleGrantedAuthority(role.name()));
	}
	
	public boolean isAccountNonExpired() {
	    return true;
	}

	public boolean isAccountNonLocked() {
	    return true;
	}

	public boolean isCredentialsNonExpired() {
	    return true;
	}

	public boolean isEnabled() {
	    return true;
	}
}
