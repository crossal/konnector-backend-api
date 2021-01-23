package com.konnector.backendapi.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserService {
	User createUser(User user);
	User getUser(Long id);
//	User updateUser(User user);
//	User mergeUser(User user);
//	void deleteUser(long id);
}
