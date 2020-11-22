package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorImplTest {

	@InjectMocks
	private UserValidator userValidator = new UserValidatorImpl();

	@Mock
	private UserRepository userRepositoryMock;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final User user = podamFactory.manufacturePojo(User.class);

	@BeforeEach
	public void setup() {
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
	public void validateUserCreationArgument_emailInUse_throwsException() {
		Optional<User> optionalUser = Optional.of(user);
		when(userRepositoryMock.findByEmail(user.getEmail())).thenReturn(optionalUser);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(user));
	}
}
