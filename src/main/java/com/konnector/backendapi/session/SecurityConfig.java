package com.konnector.backendapi.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

	private static final int BCRYPT_STRENGTH = 12;
	private static final int ONE_WEEK_IN_SECONDS = 604800;

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	@Lazy
	private RememberMeServices rememberMeServices;
	@Autowired
	@Lazy
	private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
	@Autowired
	private Environment environment;

	@Value("${server.servlet.session.remember.me.key}")
	private String rememberMeKey;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		authenticationManagerBuilder.authenticationProvider(rememberMeAuthenticationProvider);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(rememberMeKey, userDetailsService);
		tokenBasedRememberMeServices.setAlwaysRemember(true);
		tokenBasedRememberMeServices.setTokenValiditySeconds(ONE_WEEK_IN_SECONDS);
		tokenBasedRememberMeServices.setUseSecureCookie(true);
		return tokenBasedRememberMeServices;
	}

	@Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
		return new RememberMeAuthenticationProvider(rememberMeKey);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.requiresChannel(channel -> {
					if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env ->
							env.equalsIgnoreCase(com.konnector.backendapi.Environment.PROD.getValue()))) {
						channel.anyRequest().requiresSecure();
					}
				})
				.csrf().disable() // disabled as solved via SameSite settings
				.authorizeHttpRequests()
				.requestMatchers(HttpMethod.GET, "/api/health").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/users").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/verifications/verify*").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/verifications*").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
				.requestMatchers(HttpMethod.GET, "/verifications*").permitAll()
				.requestMatchers("/api/**").authenticated() // comment this line out for testing
				.anyRequest().permitAll()
				.and()
				.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				.and()
				.logout().deleteCookies("JSESSIONID")
				.and()
				.rememberMe().rememberMeServices(rememberMeServices)
				.and()
				.sessionManagement()
				.sessionFixation().migrateSession()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER)
				.maximumSessions(1);
		return http.build();
	}

	/**
	 * Redirect unmapped URLs to the home page/index
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/{spring:\\w+}")
				.setViewName("forward:/");
		registry.addViewController("/**{spring:\\w+}")
				.setViewName("forward:/");
	}
}
