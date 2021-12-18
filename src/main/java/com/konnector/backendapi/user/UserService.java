package com.konnector.backendapi.user;

public interface UserService {
	User createUser(User user);
	User getUser(Long id);
	void updateUserPassword(User user, String password);
	User updateUser(User user, Long userId, String oldPassword);
}
