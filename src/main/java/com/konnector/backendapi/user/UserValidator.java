package com.konnector.backendapi.user;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserValidator {
	void validateUserCreationArgument(User user);
	void validateUserUpdateArgument(User existingUser, User userArg, Long userId, String oldPassword, PasswordEncoder passwordEncoder);
	void validateUserFetchRequest(Long userId);
	void validateConnectionsFetchRequest(Long userId, Integer pageNumber, Integer pageSize);
	void validateConnectionsCountFetchRequest(Long userId);
}
