package com.konnector.backendapi.login;

import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.exceptions.InvalidLoginDetailsException;
import com.konnector.backendapi.security.SecurityService;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

	@InjectMocks
	private final LoginService loginService = new LoginServiceImpl();

	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private SecurityService securityServiceMock;

	@Mock
	private User userMock;

	private final static String EMAIL = "email";
	private final static String PASSWORD = "password";

	@Test
	public void login_logsInUser() {
		when(userMock.isEmailVerified()).thenReturn(true);
		when(userRepositoryMock.findByEmailOrUsername(EMAIL, EMAIL)).thenReturn(Optional.of(userMock));

		User result = loginService.login(EMAIL, PASSWORD);

		assertEquals(userMock, result);
		verify(securityServiceMock, times(1)).createSession(EMAIL, PASSWORD);
	}

	@Test
	public void login_userNotFound_throwsException() {
		assertThrows(InvalidLoginDetailsException.class, () -> loginService.login(EMAIL, PASSWORD));
	}

	@Test
	public void login_userNotVerified_throwsException() {
		when(userMock.isEmailVerified()).thenReturn(false);
		when(userRepositoryMock.findByEmailOrUsername(EMAIL, EMAIL)).thenReturn(Optional.of(userMock));

		assertThrows(InvalidDataException.class, () -> loginService.login(EMAIL, PASSWORD));
	}
}
