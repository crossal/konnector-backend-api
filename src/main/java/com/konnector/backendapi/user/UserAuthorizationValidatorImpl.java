package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserAuthorizationValidatorImpl implements UserAuthorizationValidator {

	@Override
	public void validateUserFetchRequest(User user, Authentication authentication) {
		if (!user.getEmail().equals(authentication.getName()) && !user.getUsername().equals(authentication.getName())) {
			throw new UnauthorizedException();
		}
	}
}
