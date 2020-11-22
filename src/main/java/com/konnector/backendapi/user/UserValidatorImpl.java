package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserValidatorImpl implements UserValidator {

	private static final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
			"[a-zA-Z0-9_+&*-]+)*@" +
			"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
			"A-Z]{2,7}$";
	private static final Pattern emailPattern = Pattern.compile(emailRegex);

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
		if (!emailPattern.matcher(user.getEmail()).matches()){
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

		Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
		optionalUser.ifPresent(existingUser -> {
			throw new InvalidDataException("Email already used");
		});
	}
}
