package com.konnector.backendapi.user;

import java.util.List;
import java.util.Optional;

public interface UserService {
	User createUser(User user);
	User getUser(Long id);
	void updateUserPassword(User user, String password);
	User updateUser(User user, Long userId, String oldPassword);
	List<User> getUsers(Optional<Long> userId, boolean connectedUsers, String username, Integer pageNumber, Integer pageSize);
	long getUsersCount(Optional<Long> userId, boolean connectedUsers, String username);
}
