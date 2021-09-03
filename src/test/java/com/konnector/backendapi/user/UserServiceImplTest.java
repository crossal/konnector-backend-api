package com.konnector.backendapi.user;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.notifications.EmailNotificationService;
import com.konnector.backendapi.verification.Verification;
import com.konnector.backendapi.verification.VerificationService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@InjectMocks
	private final UserService userService = new UserServiceImpl();

	@Mock
	private Dao<User> userDaoMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private UserValidator userValidatorMock;
	@Mock
	private VerificationService verificationServiceMock;
	@Mock
	private EmailNotificationService emailNotificationServiceMock;
	@Mock
	private PasswordEncoder passwordEncoderMock;
	@Mock
	private Verification verificationMock;
	@Mock
	private AuthenticationFacade authenticationFacadeMock;
	@Mock
	private UserAuthorizationValidator userAuthorizationValidatorMock;
	@Mock
	private Authentication authenticationMock;
	@Mock
	private User userMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final User user = easyRandom.nextObject(User.class);
	private final String hashedPassword = "hashed_password";
	private final String verificationCode = "1234";
	private final String verificationUrlToken = "5678";
	private final String password = "password";

	@Test
	public void createUser_createsUser() {
		when(passwordEncoderMock.encode(user.getPassword())).thenReturn(hashedPassword);
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				ReflectionTestUtils.setField(user, "id", 1L);
				return null;
			}
		}).when(userDaoMock).save(user);

		User createdUser = userService.createUser(user);

		verify(userValidatorMock, times(1)).validateUserCreationArgument(user);
		verify(userDaoMock, times(1)).save(user);
		verify(verificationServiceMock, times(1)).createEmailVerificationForUser(user);
		assertEquals(user, createdUser);
		assertEquals(hashedPassword, user.getPassword());
	}

	@Test
	public void getUser_userNotFound_throwsException() {
		Long userId = 1L;
		when(userDaoMock.get(userId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> userService.getUser(userId));

		verify(userValidatorMock, times(1)).validateUserFetchRequest(userId);
		verify(userDaoMock, times(1)).get(userId);
	}

	@Test
	public void getUser_getsUser() {
		Long userId = 1L;
		when(userDaoMock.get(userId)).thenReturn(Optional.of(userMock));
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		User fetchedUser = userService.getUser(userId);

		assertEquals(userMock, fetchedUser);
		verify(userValidatorMock, times(1)).validateUserFetchRequest(userId);
		verify(userDaoMock, times(1)).get(userId);
		verify(userAuthorizationValidatorMock, times(1)).validateUserRequest(userId, authenticationMock);
	}

	@Test
	public void updateUser_userNotFound_throwsException() {
		Long userId = 1L;
		when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> userService.updateUser(userMock, userId, null));
	}

	@Test
	public void updateUser_updatesUser() {
		Long userId = 1L;
		when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(userMock));
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		String password = "password";
		when(userMock.getPassword()).thenReturn(password);
		String hashedPassword = "password_hashed";
		when(passwordEncoderMock.encode(password)).thenReturn(hashedPassword);
		String oldPassword = "old_password";
		String oldPasswordHashed = "old_password_hashed";
		when(passwordEncoderMock.encode(oldPassword)).thenReturn(oldPasswordHashed);

		User updatedUser = userService.updateUser(userMock, userId, oldPassword);

		assertEquals(userMock, updatedUser);
		verify(userValidatorMock, times(1)).validateUserUpdateArgument(userMock, userMock, userId, oldPasswordHashed);
		verify(userAuthorizationValidatorMock, times(1)).validateUserRequest(userId, authenticationMock);
		verify(userMock).setPassword(hashedPassword);
		verify(userMock).merge(userMock);
		verify(userDaoMock).update(userMock);
	}

	@Test
	public void updateUserPassword_updatesUserPassword() {
		when(passwordEncoderMock.encode(password)).thenReturn(hashedPassword);

		userService.updateUserPassword(userMock, password);

		verify(userMock).validatePassword(password);
		verify(passwordEncoderMock).encode(password);
		verify(userMock).setPassword(hashedPassword);
		verify(userDaoMock).update(userMock);
	}
}
