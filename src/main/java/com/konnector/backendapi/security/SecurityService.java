package com.konnector.backendapi.security;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
	void createSession(HttpServletRequest request, String username, String password);
}
