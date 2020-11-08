package com.konnector.backendapi.user;

import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.security.HashedPassword;
import com.konnector.backendapi.security.PasswordHashingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private Dao<User> userDao;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private PasswordHashingService passwordHashingService;

	@Override
	@Transactional
	public User createUser(User user, String password) {
		userValidator.validateUserCreationArgument(user);
		HashedPassword hashedPassword = passwordHashingService.getHashedPassword(password);
		user.setPassword(hashedPassword.getHash());
		user.setSalt(hashedPassword.getSalt());
		userDao.save(user);
		return user;
	}
}
