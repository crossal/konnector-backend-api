package com.konnector.backendapi.user;

public interface UserService {
	User createUser(User user);
	User getUser(Long id);
	void updateUserPassword(User user, String password);
	User updateUser(User user, Long id, String oldPassword);
//	User mergeUser(User user);
//	void deleteUser(long id);
}
