package com.konnector.backendapi.user;

import org.springframework.security.core.Authentication;

public interface UserAuthorizationValidator {
	void validateUserFetchRequest(User user, Authentication authentication);
}
