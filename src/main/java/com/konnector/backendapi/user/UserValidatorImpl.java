package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserValidatorImpl implements UserValidator {

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
	public void validateUserUpdateArgument(User user) {
		if (user == null) {
			throw new InvalidDataException("User cannot be empty.");
		}

		user.validateForUpdate();
	}

	@Override
	public void validateUserFetchRequest(Long userId) {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}
	}
}
