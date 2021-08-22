package com.konnector.backendapi.user;

public interface UserValidator {
	void validateUserCreationArgument(User user);
	void validateUserUpdateArgument(User existingUser, User userArg, Long userId);
	void validateUserFetchRequest(Long userId);
}
