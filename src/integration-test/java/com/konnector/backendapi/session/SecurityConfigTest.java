package com.konnector.backendapi.session;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class SecurityConfigTest {

	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String USER_ROLE = "USER";

	@Bean
	@Primary
	public UserDetailsService userDetailsService() {
		List<UserDetails> userDetailsList = new ArrayList<>();
		userDetailsList.add(User.withUsername(USERNAME).password(PASSWORD).roles(USER_ROLE).build());

		return new InMemoryUserDetailsManager(userDetailsList);
	}

	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return rawPassword.toString().equals(encodedPassword);
			}
		};
	}
}
