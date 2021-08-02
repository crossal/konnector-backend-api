package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorImplTest {

	@InjectMocks
	private UserValidator userValidator = new UserValidatorImpl();

	@Mock
	private UserRepository userRepositoryMock;

	@Mock
	private User userMock;

	@Test
	public void validateUserCreationArgument_userIsValid_doesNotThrowException() {
		userValidator.validateUserCreationArgument(userMock);
		verify(userMock, times(1)).validateForCreation();
	}

	@Test
	public void validateUserCreationArgument_userIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(null));
	}

	@Test
	public void validateUserCreationArgument_emailOrUsernameInUse_throwsException() {
		String email = "user@email.com";
		String username = "username";
		when(userMock.getEmail()).thenReturn(email);
		when(userMock.getUsername()).thenReturn(username);
		Optional<User> optionalUser = Optional.of(userMock);
		when(userRepositoryMock.findByEmailOrUsername(email, username)).thenReturn(optionalUser);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserCreationArgument(userMock));
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
