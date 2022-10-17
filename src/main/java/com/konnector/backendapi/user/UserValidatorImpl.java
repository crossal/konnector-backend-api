package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserValidatorImpl implements UserValidator {

	private static final int MAX_PAGE_SIZE = 10;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void validateUserCreationArgument(User user) {
		if (user == null) {
			throw new InvalidDataException("User cannot be empty.");
		}

		user.validateForCreation();

		Optional<User> optionalUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
		optionalUser.ifPresent(existingUser -> {
			if (existingUser.getEmail().equals(user.getEmail())) {
				throw new InvalidDataException("Email taken.");
			} else {
				throw new InvalidDataException("Username taken.");
			}
		});
	}

	@Override
	public void validateUserUpdateArgument(User existingUser, User userArg, Long userId, String oldPassword, PasswordEncoder passwordEncoder) {
		if (userArg == null) {
			throw new InvalidDataException("User cannot be empty.");
		}

		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}

		userArg.validateForUpdate();

		if (!userArg.getId().equals(userId)) {
			throw new InvalidDataException("User Id does not equal Id in path.");
		}

		if (!existingUser.getEmail().equals(userArg.getEmail())) {
			throw new InvalidDataException("Cannot update email address.");
		}

		if (!existingUser.getUsername().equals(userArg.getUsername())) {
			throw new InvalidDataException("Cannot update username.");
		}

		if (existingUser.isEmailVerified() != userArg.isEmailVerified()) {
			throw new InvalidDataException("Cannot update verification status.");
		}

		if (userArg.getPassword() != null && !userArg.getPassword().isEmpty()) {
			if (oldPassword == null || oldPassword.isEmpty()) {
				throw new InvalidDataException("Cannot set new password without current password.");
			} else if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
				throw new InvalidDataException("Cannot set new password as current password provided is incorrect.");
			}
		}
	}

	@Override
	public void validateUserFetchRequest(Long userId) {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}
	}

	@Override
	public void validateUsersFetchRequest(Integer pageNumber, Integer pageSize) {
		if (pageNumber == null) {
			throw new InvalidDataException("Page number cannot be empty.");
		}

		if (pageSize == null) {
			throw new InvalidDataException("Page size cannot be empty.");
		}

		if (pageSize > MAX_PAGE_SIZE) {
			throw new InvalidDataException("Page size cannot be larger than " + MAX_PAGE_SIZE + ".");
		}
	}
}
