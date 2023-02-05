package com.konnector.backendapi.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@SuppressWarnings("serial")
public class SecurityUser extends User {

	private final Long userId;

	public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId) {
		super(username, password, authorities);
		this.userId = userId;
	}

	public SecurityUser(UserDetails userDetails, Long userId) {
		super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
