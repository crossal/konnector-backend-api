package com.konnector.backendapi.session;

import com.konnector.backendapi.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final int BCRYPT_STRENGTH = 12;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/api/health").permitAll()
				.antMatchers(HttpMethod.POST, "/api/users").permitAll()
				.antMatchers(HttpMethod.POST, "/api/verifications/verify*").permitAll()
				.antMatchers(HttpMethod.POST, "/api/verifications*").permitAll()
				.antMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
				.antMatchers("/api/**").authenticated() // comment this line out for testing
				.anyRequest().permitAll()
				.and()
				.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				.and()
				.logout().deleteCookies("JSESSIONID")
				.and()
				.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400)
				.and()
				.sessionManagement()
				.sessionFixation().migrateSession()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER)
				.maximumSessions(1);
	}
}
