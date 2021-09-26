package com.konnector.backendapi.user;

import org.springframework.security.core.Authentication;

public interface UserAuthorizationValidator {
	void validateUserRequest(Long userId, Authentication authentication);
}
