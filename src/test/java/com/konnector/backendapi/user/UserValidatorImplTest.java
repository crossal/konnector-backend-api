package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorImplTest {

	@InjectMocks
	private UserValidator userValidator = new UserValidatorImpl();

	@Mock
	private UserRepository userRepositoryMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final User user = easyRandom.nextObject(User.class);

	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(user, "id", null);
		user.setEmail("some_email@validationtest.com");
	}

	@Test
	public void validateUserCreationArgument_userIsValid_doesNotThrowException() {
		userValidator.validateUserCreationArgument(user);
	}

	@Test
	public void validateUserCreationArgument_userIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(null));
	}

	@Test
	public void validateUserCreationArgument_idIsNotNull_throwsException() {
		ReflectionTestUtils.setField(user, "id", 1L);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(null));
	}

	@Test
	public void validateUserCreationArgument_emailIsNull_throwsException() {
		user.setEmail(null);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserCreationArgument_emailIsInvalid_throwsException() {
		user.setEmail("invalid_email");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserCreationArgument_usernameIsNull_throwsException() {
		user.setUsername(null);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserCreationArgument_firstNameIsNull_throwsException() {
		user.setFirstName(null);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserCreationArgument_lastNameIsNull_throwsException() {
		user.setLastName(null);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserCreationArgument_passwordIsNull_throwsException() {
		user.setPassword(null);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserCreationArgument_passwordIsTooLong_throwsException() {
		user.setPassword("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserCreationArgument_emailOrUsernameInUse_throwsException() {
		Optional<User> optionalUser = Optional.of(user);
		when(userRepositoryMock.findByEmailOrUsername(user.getEmail(), user.getUsername())).thenReturn(optionalUser);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}

	@Test
	public void validateUserFetchRequest_emptyUserId_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserFetchRequest(null));
	}

	@Test
	public void validateUserFetchRequest_requestIsValid_doesNotThrowException() {
		userValidator.validateUserFetchRequest(1L);
	}
}
