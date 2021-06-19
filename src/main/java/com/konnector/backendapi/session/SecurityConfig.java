package com.konnector.backendapi.session;

import com.konnector.backendapi.security.SessionCookieFilter;
import com.konnector.backendapi.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
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
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final int BCRYPT_STRENGTH = 12;

//	@Autowired
//	private UserDetailsService userDetailsService;

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth
//				.inMemoryAuthentication()
//				.withUser("user")
//				.password("password")
//				.roles("USER")
//				.and()
//				.withUser("admin")
//				.password("password")
//				.roles("ADMIN", "USER");
//	}

//	@Bean
//	public ServletContextInitializer servletContextInitializer() {
//		return new ServletContextInitializer() {
//			@Override
//			public void onStartup(ServletContext servletContext) throws ServletException {
//				servletContext.getSessionCookieConfig().setSecure(true);
//			}
//		};
//	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new Pbkdf2PasswordEncoder();
//	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService);
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
//				.antMatchers(HttpMethod.GET, "/api/users/*").permitAll()
				.antMatchers("/api/**").authenticated() // comment this line out for testing
				.anyRequest().permitAll()
//				.anyRequest().authenticated()
				.and()
				.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				.and()
//				.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPointImpl())
//				.and()
//				.exceptionHandling().accessDeniedHandler(new AccessDeniedExceptionHandler())
//				.and()
				.addFilterAfter(new SessionCookieFilter(), BasicAuthenticationFilter.class)
//				.addFilter(new SessionCookieFilter())
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
