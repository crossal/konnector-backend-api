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
	@Mock
	private User existingUserMock;

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
	public void validateUserUpdateArgument_argumentsAreValid_doesNotThrowException() {
		when(userMock.getId()).thenReturn(1L);
		when(userMock.getEmail()).thenReturn("test@email.com");
		when(userMock.getUsername()).thenReturn("username");
		userValidator.validateUserUpdateArgument(userMock, userMock, 1L);
		verify(userMock, times(1)).validateForUpdate();
	}

	@Test
	public void validateUserUpdateArgument_userArgIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(userMock, null, 1L));
	}

	@Test
	public void validateUserUpdateArgument_userArgIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(userMock, userMock, null));
	}

	@Test
	public void validateUserUpdateArgument_userArgIdDoesNotEqualUserId_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(userMock, userMock, 2L));
	}

	@Test
	public void validateUserUpdateArgument_existingUserEmailDoesNotEqualUserArgEmail_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		when(existingUserMock.getEmail()).thenReturn("test1@email.com");
		when(userMock.getEmail()).thenReturn("test2@email.com");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L));
	}

	@Test
	public void validateUserUpdateArgument_existingUserUsernameDoesNotEqualUserArgUsername_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		String email = "test1@email.com";
		when(existingUserMock.getEmail()).thenReturn(email);
		when(userMock.getEmail()).thenReturn(email);
		when(existingUserMock.getUsername()).thenReturn("username1");
		when(userMock.getUsername()).thenReturn("username2");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L));
	}

	@Test
	public void validateUserUpdateArgument_existingUserVerificationDoesNotEqualUserArgVerification_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		String email = "test1@email.com";
		when(existingUserMock.getEmail()).thenReturn(email);
		when(userMock.getEmail()).thenReturn(email);
		String username = "username1";
		when(existingUserMock.getUsername()).thenReturn(username);
		when(userMock.getUsername()).thenReturn(username);
		when(existingUserMock.isEmailVerified()).thenReturn(false);
		when(userMock.isEmailVerified()).thenReturn(true);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L));
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
