package com.konnector.backendapi.security;

public interface SecurityService {
	void createSession(String username, String password);
}
