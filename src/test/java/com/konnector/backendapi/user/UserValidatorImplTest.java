package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	private PasswordEncoder passwordEncoderMock;

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
		String password = "password";
		when(userMock.getId()).thenReturn(1L);
		when(userMock.getEmail()).thenReturn("test@email.com");
		when(userMock.getUsername()).thenReturn("username");
		when(userMock.getPassword()).thenReturn(password);
		when(passwordEncoderMock.matches(password, password)).thenReturn(true);
		userValidator.validateUserUpdateArgument(userMock, userMock, 1L, password, passwordEncoderMock);
		verify(userMock, times(1)).validateForUpdate();
	}

	@Test
	public void validateUserUpdateArgument_userArgIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(userMock, null, 1L, null, passwordEncoderMock));
	}

	@Test
	public void validateUserUpdateArgument_userArgIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(userMock, userMock, null, null, passwordEncoderMock));
	}

	@Test
	public void validateUserUpdateArgument_userArgIdDoesNotEqualUserId_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(userMock, userMock, 2L, null, passwordEncoderMock));
	}

	@Test
	public void validateUserUpdateArgument_existingUserEmailDoesNotEqualUserArgEmail_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		when(existingUserMock.getEmail()).thenReturn("test1@email.com");
		when(userMock.getEmail()).thenReturn("test2@email.com");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L, null, passwordEncoderMock));
	}

	@Test
	public void validateUserUpdateArgument_existingUserUsernameDoesNotEqualUserArgUsername_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		String email = "test1@email.com";
		when(existingUserMock.getEmail()).thenReturn(email);
		when(userMock.getEmail()).thenReturn(email);
		when(existingUserMock.getUsername()).thenReturn("username1");
		when(userMock.getUsername()).thenReturn("username2");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L, null, passwordEncoderMock));
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
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L, null, passwordEncoderMock));
	}

	@Test
	public void validateUserUpdateArgument_newPasswordProvidedButCurrentPasswordNotProvided_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		String email = "test1@email.com";
		when(existingUserMock.getEmail()).thenReturn(email);
		when(userMock.getEmail()).thenReturn(email);
		String username = "username1";
		when(existingUserMock.getUsername()).thenReturn(username);
		when(userMock.getUsername()).thenReturn(username);
		when(existingUserMock.isEmailVerified()).thenReturn(true);
		when(userMock.isEmailVerified()).thenReturn(true);
		when(userMock.getPassword()).thenReturn("password");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L, null, passwordEncoderMock));
	}

	@Test
	public void validateUserUpdateArgument_newPasswordProvidedButCurrentPasswordIsIncorrect_throwsException() {
		when(userMock.getId()).thenReturn(1L);
		String email = "test1@email.com";
		when(existingUserMock.getEmail()).thenReturn(email);
		when(userMock.getEmail()).thenReturn(email);
		String username = "username1";
		when(existingUserMock.getUsername()).thenReturn(username);
		when(userMock.getUsername()).thenReturn(username);
		when(existingUserMock.isEmailVerified()).thenReturn(true);
		when(userMock.isEmailVerified()).thenReturn(true);
		when(userMock.getPassword()).thenReturn("password");
		when(existingUserMock.getPassword()).thenReturn("password");
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserUpdateArgument(existingUserMock, userMock, 1L, "incorrect_password", passwordEncoderMock));
	}

	@Test
	public void validateUserFetchRequest_emptyUserId_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUserFetchRequest(null));
	}

	@Test
	public void validateUserFetchRequest_requestIsValid_doesNotThrowException() {
		userValidator.validateUserFetchRequest(1L);
	}

	@Test
	public void validateUsersFetchRequest_pageNumberIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUsersFetchRequest(null, 1));
	}

	@Test
	public void validateUsersFetchRequest_pageSizeIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> userValidator.validateUsersFetchRequest(1, null));
	}

	@Test
	public void validateUsersFetchRequest_argsAreValid_doesNotThrowException() {
		userValidator.validateUsersFetchRequest(1, 1);
	}
}
