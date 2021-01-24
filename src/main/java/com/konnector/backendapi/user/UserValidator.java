package com.konnector.backendapi.user;

import org.springframework.security.core.Authentication;

public interface UserValidator {
	void validateUserCreationArgument(User user);
	void validateUserFetchRequest(Long userId);
}
