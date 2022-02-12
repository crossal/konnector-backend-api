package com.konnector.backendapi.user;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserAuthorizationValidator {
	void validateUserRequest(Long userId, Authentication authentication);
	void validateUserRequest(List<Long> userIds, Authentication authentication);
}
