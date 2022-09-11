package com.konnector.backendapi.login;

import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.exceptions.InvalidLoginDetailsException;
import com.konnector.backendapi.exceptions.UserNotVerifiedException;
import com.konnector.backendapi.security.SecurityService;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SecurityService securityService;

	@Override
	public User login(String usernameOrEmail, String password) {
		if (usernameOrEmail == null) {
			throw new InvalidDataException("Username/email cannot be empty.");
		}

		if (password == null) {
			throw new InvalidDataException("Password cannot be empty.");
		}

		Optional<User> optionalUser = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);

		User user = optionalUser.orElseGet(() -> { throw new InvalidLoginDetailsException("User not found."); });

		if (!user.isEmailVerified()) {
			throw new UserNotVerifiedException("User not verified.");
		}

		securityService.createSession(usernameOrEmail, password);

		return user;
	}
}
