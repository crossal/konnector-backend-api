package com.konnector.backendapi.user;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.verification.VerificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private Dao<User> userDao;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private VerificationService verificationService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private UserAuthorizationValidator userAuthorizationValidator;

	@Override
	@Transactional
	public User createUser(User user) {
		userValidator.validateUserCreationArgument(user);

		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);

		userDao.save(user);

		verificationService.createEmailVerificationForUser(user);

		return user;
	}

	@Override
	@Transactional
	public User updateUser(User user, Long id) {
		Optional<User> optionalUser = userRepository.findById(id);

		return optionalUser.map(
				existingUser -> {
					userValidator.validateUserUpdateArgument(existingUser, user, id);

					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(id, authentication);

					existingUser.merge(user);
					userDao.update(existingUser);

					return existingUser;
				}
		).orElseThrow(() -> new NotFoundException("User not found."));
	}

	@Override
	public User getUser(Long id) {
		userValidator.validateUserFetchRequest(id);

		Optional<User> optionalUser = userDao.get(id);

		return optionalUser.map(
				user -> {
					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(id, authentication);

					return user;
				}
		).orElseThrow(() -> new NotFoundException("User not found."));
	}

	@Override
	public void updateUserPassword(User user, String password) {
		String hashedPassword = passwordEncoder.encode(password);
		user.setPassword(hashedPassword);

		userDao.save(user);
	}
}
