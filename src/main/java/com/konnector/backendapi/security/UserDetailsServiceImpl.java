package com.konnector.backendapi.security;

import com.konnector.backendapi.exceptions.InvalidLoginDetailsException;
import com.konnector.backendapi.role.RoleType;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);

		return optionalUser.map(user -> new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				getAuthorities()
		)).orElseThrow(() -> new InvalidLoginDetailsException("Invalid username or email"));
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<>();

		list.add(new SimpleGrantedAuthority(RoleType.USER.name()));

		return list;
	}
}
