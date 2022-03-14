package com.konnector.backendapi.user;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.security.AuthenticationUtil;
import com.konnector.backendapi.verification.VerificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
	public User updateUser(User user, Long userId, String oldPassword) {
		Optional<User> optionalUser = userRepository.findById(userId);

		return optionalUser.map(
				existingUser -> {
					userValidator.validateUserUpdateArgument(existingUser, user, userId, oldPassword, passwordEncoder);

					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(userId, authentication);

					if (user.getPassword() != null && !user.getPassword().isEmpty()) {
						String hashedPassword = passwordEncoder.encode(user.getPassword());
						user.setPassword(hashedPassword);
					}

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
		user.validatePassword(password);
		String hashedPassword = passwordEncoder.encode(password);
		user.setPassword(hashedPassword);

		userDao.update(user);
	}

	@Override
	public List<User> getConnections(Long userId, Integer pageNumber, Integer pageSize) {
		userValidator.validateConnectionsFetchRequest(userId, pageNumber, pageSize);

		Authentication authentication = authenticationFacade.getAuthentication();
		userAuthorizationValidator.validateUserRequest(userId, authentication);

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
		Page page = userRepository.getConnections(userId, pageable);

		return page.getContent();
	}

	@Override
	@Transactional
	public long getConnectionsCount(Long userId) {
		userValidator.validateConnectionsCountFetchRequest(userId);

		Authentication authentication = authenticationFacade.getAuthentication();
		userAuthorizationValidator.validateUserRequest(userId, authentication);

		return userRepository.countConnectionsByUserId(userId);
	}

	@Override
	public List<User> getUsers(Integer pageNumber, Integer pageSize) {
		userValidator.validateUsersFetchRequest(pageNumber, pageSize);

		Authentication authentication = authenticationFacade.getAuthentication();
		Long userId = AuthenticationUtil.getUserId(authentication);

		Pageable sortedByNamePageable = PageRequest.of(pageNumber - 1, pageSize,
				Sort.by(
						Sort.Order.asc("firstName"),
						Sort.Order.asc("lastName"),
						Sort.Order.asc("username"))
		);
		Page page = userRepository.findByIdNot(userId, sortedByNamePageable);

		return page.getContent();
	}

	@Override
	@Transactional
	public long getUsersCount() {
		Authentication authentication = authenticationFacade.getAuthentication();
		Long userId = AuthenticationUtil.getUserId(authentication);

		return userRepository.countByIdNot(userId);
	}
}
