package com.konnector.backendapi.user;

import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.notifications.EmailNotificationService;
import com.konnector.backendapi.security.password.HashedPassword;
import com.konnector.backendapi.security.password.PasswordHashingService;
import com.konnector.backendapi.verification.Verification;
import com.konnector.backendapi.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private Dao<User> userDao;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private VerificationService verificationService;
	@Autowired
	private PasswordHashingService passwordHashingService;
	@Autowired
	private EmailNotificationService emailNotificationService;

	@Override
	@Transactional
	public User createUser(User user, String password) {
		userValidator.validateUserCreationArgument(user);

		HashedPassword hashedPassword = passwordHashingService.getHashedPassword(password);
		user.setPassword(hashedPassword.getHash());
		user.setSalt(hashedPassword.getSalt());

		userDao.save(user);

		Verification verification = verificationService.createEmailVerificationForUser(user.getId());
		emailNotificationService.sendVerificationEmail(user.getEmail(), verification.getCode(), verification.getUrlToken());

		return user;
	}
}
