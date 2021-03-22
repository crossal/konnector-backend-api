package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

	private final EasyRandom easyRandom = new EasyRandom();
	private final User user = easyRandom.nextObject(User.class);

	@Test
	public void validateUserCreationArgument_idIsNotNull_throwsException() {
		ReflectionTestUtils.setField(user, "id", 1L);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_emailIsNull_throwsException() {
		user.setEmail(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_emailIsEmpty_throwsException() {
		user.setEmail("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_emailIsInvalid_throwsException() {
		user.setEmail("invalid_email");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_usernameIsNull_throwsException() {
		user.setUsername(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_usernameIsEmpty_throwsException() {
		user.setUsername("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_firstNameIsNull_throwsException() {
		user.setFirstName(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_firstNameIsEmpty_throwsException() {
		user.setFirstName("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_lastNameIsNull_throwsException() {
		user.setLastName(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_lastNameIsEmpty_throwsException() {
		user.setLastName("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_passwordIsNull_throwsException() {
		user.setPassword(null);
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_passwordIsEmpty_throwsException() {
		user.setPassword("");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}

	@Test
	public void validateUserCreationArgument_passwordIsTooLong_throwsException() {
		user.setPassword("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
		assertThrows(InvalidDataException.class, () -> user.validateForCreation());
	}
}
