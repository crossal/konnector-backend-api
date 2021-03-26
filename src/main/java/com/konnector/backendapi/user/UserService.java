package com.konnector.backendapi.user;

public interface UserService {
	User createUser(User user);
	User getUser(Long id);
	void updateUserPassword(User user, String password);
//	User updateUser(User user);
//	User mergeUser(User user);
//	void deleteUser(long id);
}
