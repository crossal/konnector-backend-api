package com.konnector.backendapi.user;

import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.notifications.EmailNotificationService;
import com.konnector.backendapi.verification.Verification;
import com.konnector.backendapi.verification.VerificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	private EmailNotificationService emailNotificationService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public User createUser(User user) {
		userValidator.validateUserCreationArgument(user);

		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);

		userDao.save(user);

		Verification verification = verificationService.createEmailVerificationForUser(user.getId());
		emailNotificationService.sendVerificationEmail(user.getEmail(), verification.getCode(), verification.getUrlToken());

		return user;
	}

	@Override
	public User getUser(Long id) {
		userValidator.validateUserFetchRequest(id);

		Optional<User> optionalUser = userDao.get(id);

		return optionalUser.orElseThrow(() -> new NotFoundException("User not found"));
	}
}
