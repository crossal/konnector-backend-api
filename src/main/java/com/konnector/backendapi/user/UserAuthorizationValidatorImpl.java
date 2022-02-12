package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.UnauthorizedException;
import com.konnector.backendapi.security.AuthenticationUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthorizationValidatorImpl implements UserAuthorizationValidator {

	@Override
	public void validateUserRequest(Long userId, Authentication authentication) {
		validateUserRequest(List.of(userId), authentication);
	}

	@Override
	public void validateUserRequest(List<Long> userIds, Authentication authentication) {
		Long authenticatedUserId = AuthenticationUtil.getUserId(authentication);

		boolean foundAMatch = userIds.stream().anyMatch(userId -> userId.equals(authenticatedUserId));

		if (!foundAMatch) {
			throw new UnauthorizedException();
		}
	}
}
