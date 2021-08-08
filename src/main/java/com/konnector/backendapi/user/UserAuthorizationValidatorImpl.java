package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.UnauthorizedException;
import com.konnector.backendapi.security.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationValidatorImpl implements UserAuthorizationValidator {

	@Override
	public void validateUserRequest(Long userId, Authentication authentication) {
		Long authenticatedUserId = ((SecurityUser) authentication.getPrincipal()).getUserId();
		if (!userId.equals(authenticatedUserId)) {
			throw new UnauthorizedException();
		}
	}
}
