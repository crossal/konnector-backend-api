package com.konnector.backendapi.user;

public interface UserValidator {
	void validateUserCreationArgument(User user);
	void validateUserUpdateArgument(User user);
	void validateUserFetchRequest(Long userId);
}
