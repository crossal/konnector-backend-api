package com.konnector.backendapi.user;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserValidator {
	void validateUserCreationArgument(User user);
	void validateUserUpdateArgument(User existingUser, User userArg, Long userId, String oldPassword, PasswordEncoder passwordEncoder);
	void validateUserFetchRequest(Long userId);
	void validateUsersFetchRequest(Integer pageNumber, Integer pageSize);
}
