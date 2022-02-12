package com.konnector.backendapi.user;

import java.util.List;

public interface UserService {
	User createUser(User user);
	User getUser(Long id);
	void updateUserPassword(User user, String password);
	User updateUser(User user, Long userId, String oldPassword);
	List<User> getConnections(Long userId, Integer pageNumber, Integer pageSize);
	long getConnectionsCount(Long userId);
}
