package com.konnector.backendapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

//	@Autowired
//	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//		User user = userRepository.findByUsername(usernameOrEmail);
//		if (user == null) {
//			user = userRepository.findByEmail(usernameOrEmail);
//		}
//
//		if (user == null) {
//			throw new UsernameNotFoundException(usernameOrEmail);
//		}
//
//		UserDetails userDetails = User.withUsername(customer.getEmail()).password(customer.getPassword()).authorities("USER").build();
//		return user;

		return null;
	}
}
