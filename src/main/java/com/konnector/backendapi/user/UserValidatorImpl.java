package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserValidatorImpl implements UserValidator {

	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." +
			"[a-zA-Z0-9_+&*-]+)*@" +
			"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
			"A-Z]{2,7}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	private static final int MAX_PASSWORD_LENGTH = 50;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void validateUserCreationArgument(User user) {
		if (user == null) {
			throw new InvalidDataException("User cannot be empty");
		}
		if (user.getId() != null) {
			throw new InvalidDataException("Id should be empty");
		}
		if (user.getEmail() == null) {
			throw new InvalidDataException("Email cannot be empty");
		}
		if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()){
			throw new InvalidDataException("Email not valid");
		}
		if (user.getUsername() == null) {
			throw new InvalidDataException("Username cannot be empty");
		}
		if (user.getFirstName() == null) {
			throw new InvalidDataException("First name cannot be empty");
		}
		if (user.getLastName() == null) {
			throw new InvalidDataException("Last name cannot be empty");
		}
		if (user.getPassword() == null) {
			throw new InvalidDataException("Password cannot be empty");
		}
		if (user.getPassword().length() > MAX_PASSWORD_LENGTH) {
			throw new InvalidDataException("Password cannot be greater than " + MAX_PASSWORD_LENGTH + " characters");
		}

		Optional<User> optionalUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
		optionalUser.ifPresent(existingUser -> {
			if (existingUser.getEmail().equals(user.getEmail())) {
				throw new InvalidDataException("Email taken");
			} else {
				throw new InvalidDataException("Username taken");
			}
		});
	}

	@Override
	public void validateUserFetchRequest(Long userId) {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty");
		}
	}
}
