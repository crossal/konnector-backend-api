package com.konnector.backendapi.session;

import com.konnector.backendapi.security.SecurityUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@TestConfiguration
public class SecurityTestConfig {

	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	private static final Long USER_ID = 1L;
	private static final String USER_ROLE = "USER";

	@Bean
	@Primary
	public UserDetailsService userDetailsService() {
		List<UserDetails> userDetailsList = new ArrayList<>();
		userDetailsList.add(new SecurityUser(USERNAME, PASSWORD, List.of(new SimpleGrantedAuthority(USER_ROLE)), USER_ID));

		return new TestInMemoryUserDetailsManager(userDetailsList);
	}

	private class TestInMemoryUserDetailsManager extends InMemoryUserDetailsManager {

		public TestInMemoryUserDetailsManager(Collection<UserDetails> users) {
			super(users);
		}

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			UserDetails userDetails = super.loadUserByUsername(username);
			SecurityUser securityUser = new SecurityUser(userDetails, USER_ID);
			return securityUser;
		}
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
