package com.konnector.backendapi.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser("user")
				.password("password")
				.roles("USER")
				.and()
				.withUser("admin")
				.password("password")
				.roles("ADMIN", "USER");
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
				.antMatchers("/api/health").permitAll()
				.antMatchers("/api/*").authenticated()
				.anyRequest().permitAll()
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
