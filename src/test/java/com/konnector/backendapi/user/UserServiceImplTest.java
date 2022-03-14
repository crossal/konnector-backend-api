package com.konnector.backendapi.user;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.security.SecurityUser;
import com.konnector.backendapi.verification.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
	private PasswordEncoder passwordEncoderMock;
	@Mock
	private AuthenticationFacade authenticationFacadeMock;
	@Mock
	private UserAuthorizationValidator userAuthorizationValidatorMock;
	@Mock
	private Authentication authenticationMock;
	@Mock
	private User userMock;
	@Mock
	private Page pageMock;
	@Mock
	private SecurityUser securityUserMock;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	private final String hashedPassword = "hashed_password";
	private final String password = "password";

	@Test
	public void createUser_createsUser() {
		when(userMock.getPassword()).thenReturn(password);
		when(passwordEncoderMock.encode(password)).thenReturn(hashedPassword);

		User createdUser = userService.createUser(userMock);

		verify(userValidatorMock).validateUserCreationArgument(userMock);
		verify(userDaoMock).save(userMock);
		verify(verificationServiceMock).createEmailVerificationForUser(userMock);
		verify(userMock).setPassword(hashedPassword);
		assertEquals(userMock, createdUser);
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

		User updatedUser = userService.updateUser(userMock, userId, oldPassword);

		assertEquals(userMock, updatedUser);
		verify(userValidatorMock, times(1)).validateUserUpdateArgument(userMock, userMock, userId, oldPassword, passwordEncoderMock);
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

	@Test
	public void getConnections_getsConnections() {
		Long userId = 1L;
		Integer pageNumber = 2;
		Integer pageSize = 5;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		List<User> connections = List.of(userMock);
		when(userRepositoryMock.getConnections(eq(userId), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(connections);

		List<User> returnedConnections = userService.getConnections(userId, pageNumber, pageSize);

		assertEquals(connections, returnedConnections);
		verify(userValidatorMock).validateConnectionsFetchRequest(userId, pageNumber, pageSize);
		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(userRepositoryMock).getConnections(eq(userId), pageableCaptor.capture());
		Pageable pageable = pageableCaptor.getValue();
		assertEquals(pageNumber - 1, pageable.getPageNumber());
		assertEquals(pageSize, pageable.getPageSize());
	}

	@Test
	public void getConnectionsCount_getsConnectionsCount() {
		long count = 10L;
		Long userId = 1L;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		when(userRepositoryMock.countConnectionsByUserId(userId)).thenReturn(count);

		long returnedConnectionsCount = userService.getConnectionsCount(userId);

		assertEquals(count, returnedConnectionsCount);
		verify(userValidatorMock).validateConnectionsCountFetchRequest(userId);
		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(userRepositoryMock).countConnectionsByUserId(userId);
	}

	@Test
	public void getUsers_getsUsers() {
		Long userId = 1L;
		Integer pageNumber = 2;
		Integer pageSize = 5;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(userId);

		List<User> users = List.of(userMock);
		when(userRepositoryMock.findByIdNot(eq(userId), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(users);

		List<User> returnedUsers = userService.getUsers(pageNumber, pageSize);

		assertEquals(users, returnedUsers);
		verify(userValidatorMock).validateUsersFetchRequest(pageNumber, pageSize);
		verify(userRepositoryMock).findByIdNot(eq(userId), pageableCaptor.capture());
		Pageable pageable = pageableCaptor.getValue();
		assertEquals(pageNumber - 1, pageable.getPageNumber());
		assertEquals(pageSize, pageable.getPageSize());
		List<Sort.Order> orders = pageable.getSort().toList();
		assertEquals(3, orders.size());
		Sort.Order order = orders.get(0);
		assertEquals("firstName", order.getProperty());
		assertEquals(Sort.Direction.ASC, order.getDirection());
		order = orders.get(1);
		assertEquals("lastName", order.getProperty());
		assertEquals(Sort.Direction.ASC, order.getDirection());
		order = orders.get(2);
		assertEquals("username", order.getProperty());
		assertEquals(Sort.Direction.ASC, order.getDirection());
	}

	@Test
	public void getUsersCount_getsUsersCount() {
		long count = 10L;
		Long userId = 1L;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(userId);

		when(userRepositoryMock.countByIdNot(userId)).thenReturn(count);

		long returnedUsersCount = userService.getUsersCount();

		assertEquals(count, returnedUsersCount);
		verify(userRepositoryMock).countByIdNot(userId);
	}
}
