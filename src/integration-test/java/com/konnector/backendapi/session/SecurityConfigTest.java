package com.konnector.backendapi.session;

import com.konnector.backendapi.role.RoleType;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@TestConfiguration
public class SecurityConfigTest {

//	@Bean
//	@Primary
//	public UserDetailsService userDetailsService() {
//		List<UserDetails> userDetailsList = new ArrayList<>();
//		userDetailsList.add(User.withUsername("user").password("password").roles("USER").build());
//
//		return new InMemoryUserDetailsManager(userDetailsList);
//	}

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
				return true;//rawPassword.toString().equals(encodedPassword);
			}
		};
	}

	@Bean
	@Primary
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return new User("username", "password", List.of(new SimpleGrantedAuthority(RoleType.USER.name())));
			}
		};
	}

//	@Bean
//	@Primary
//	public UserDetailsService userDetailsService() {
//		User basicUser = new UserImpl("Basic User", "user@company.com", "password");
//		UserActive basicActiveUser = new UserActive(basicUser, Arrays.asList(
//				new SimpleGrantedAuthority("ROLE_USER"),
//				new SimpleGrantedAuthority("PERM_FOO_READ")
//		));
//
//		User managerUser = new UserImpl("Manager User", "manager@company.com", "password");
//		UserActive managerActiveUser = new UserActive(managerUser, Arrays.asList(
//				new SimpleGrantedAuthority("ROLE_MANAGER"),
//				new SimpleGrantedAuthority("PERM_FOO_READ"),
//				new SimpleGrantedAuthority("PERM_FOO_WRITE"),
//				new SimpleGrantedAuthority("PERM_FOO_MANAGE")
//		));
//
//		return new InMemoryUserDetailsManager(Arrays.asList(
//				basicActiveUser, managerActiveUser
//		));
//	}
}
