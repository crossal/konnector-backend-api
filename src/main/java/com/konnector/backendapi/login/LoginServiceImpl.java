package com.konnector.backendapi.login;

import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.InvalidLoginDetailsException;
import com.konnector.backendapi.security.SecurityService;
import com.konnector.backendapi.security.password.PasswordHashingService;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private Dao<User> userDao;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordHashingService passwordHashingService;
	@Autowired
	private SecurityService securityService;

	@Override
	public User login(String usernameOrEmail, String password) {
		Optional<User> optionalUser = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);

		User user = optionalUser.orElseGet(() -> { throw new InvalidLoginDetailsException("User not found"); });

		// may not need this as the securityconfig will check password
//		if (!passwordHashingService.passwordMatchesHash(password, user.getSalt(), user.getPassword())) {
//			throw new InvalidLoginDetailsException("Incorrect password");
//		}

		securityService.createSession(user.getUsername(), user.getPassword().toString());

		return user;
	}
}
