package com.konnector.backendapi.user;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.security.AuthenticationUtil;
import com.konnector.backendapi.verification.VerificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	private UserRepository userRepository;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	@Lazy
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

		userRepository.save(user);

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

					userRepository.save(existingUser);

					return existingUser;
				}
		).orElseThrow(() -> new NotFoundException("User not found."));
	}

	@Override
	public User getUser(Long id) {
		userValidator.validateUserFetchRequest(id);

		Optional<User> optionalUser = userRepository.findById(id);

		return optionalUser.orElseThrow(() -> new NotFoundException("User not found."));
	}

	@Override
	public void updateUserPassword(User user, String password) {
		user.validatePassword(password);
		String hashedPassword = passwordEncoder.encode(password);
		user.setPassword(hashedPassword);

		userRepository.save(user);
	}

	@Override
	public List<User> getUsers(Optional<Long> optionalUserId, boolean connectedUsers, String username, Integer pageNumber, Integer pageSize) {
		userValidator.validateUsersFetchRequest(pageNumber, pageSize);

		Authentication authentication = authenticationFacade.getAuthentication();

		optionalUserId.ifPresent(userId -> userAuthorizationValidator.validateUserRequest(userId, authentication));
		Long userId = optionalUserId.orElseGet(() -> AuthenticationUtil.getUserId(authentication));

		Pageable sortedByNamePageable = PageRequest.of(pageNumber - 1, pageSize);

		Page<User> page;
		if (connectedUsers) {
			page = userRepository.getConnections(userId, username, sortedByNamePageable);
		} else {
			page = userRepository.getNonConnections(userId, username, sortedByNamePageable);
		}

		return page.getContent();
	}

	@Override
	@Transactional
	public long getUsersCount(Optional<Long> optionalUserId, boolean connectedUsers, String username) {
		Authentication authentication = authenticationFacade.getAuthentication();

		optionalUserId.ifPresent(userId -> userAuthorizationValidator.validateUserRequest(userId, authentication));
		Long userId = optionalUserId.orElseGet(() -> AuthenticationUtil.getUserId(authentication));

		if (connectedUsers) {
			return userRepository.countConnectionsByUserId(userId, username);
		} else {
			return userRepository.countNonConnectionsByUserId(userId, username);
		}
	}
}
