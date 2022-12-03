package com.konnector.backendapi.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class SecurityServiceImpl implements SecurityService {

	private static final Logger LOGGER = LogManager.getLogger(SecurityServiceImpl.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	@Resource
	private HttpServletRequest request;
	@Resource
	private HttpServletResponse response;
	@Autowired
	private RememberMeServices rememberMeServices;

	@Override
	public void createSession(String username, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		HttpSession session = request.getSession(true);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		rememberMeServices.loginSuccess(request, response, authentication);
	}

	@Override
	public void destroySession() {
		request.getSession(false).invalidate();
	}
}
