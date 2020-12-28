package com.konnector.backendapi.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

	private static final Logger LOGGER = LogManager.getLogger(AccessDeniedExceptionHandler.class);

	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) {
		LOGGER.error("***here");
		httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
	}
}
