package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorImpl implements UserValidator {

	@Override
	public void validateUserCreationArgument(User user) {
		if (user == null) {
			throw new InvalidDataException("User cannot be empty");
		}
		if (user.getId() != null) {
			throw new InvalidDataException("Id should be empty");
		}
		if (user.getEmail() != null) {
			throw new InvalidDataException("Email cannot be empty");
		}
		if (user.getUsername() != null) {
			throw new InvalidDataException("Username cannot be empty");
		}
		if (user.getFirstName() != null) {
			throw new InvalidDataException("First name cannot be empty");
		}
		if (user.getLastName() != null) {
			throw new InvalidDataException("Last name cannot be empty");
		}
	}
}
