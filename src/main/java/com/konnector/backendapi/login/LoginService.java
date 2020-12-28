package com.konnector.backendapi.login;

import com.konnector.backendapi.user.User;

public interface LoginService {
	User login(String usernameOrEmail, String password);
}
