package com.konnector.backendapi.connection;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.notification.NotificationService;
import com.konnector.backendapi.security.SecurityUser;
import com.konnector.backendapi.user.UserAuthorizationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceImplTest {

	@InjectMocks
	private ConnectionService connectionService = new ConnectionServiceImpl();

	@Mock
	private Dao<Connection> connectionDaoMock;
	@Mock
	private ConnectionValidator connectionValidatorMock;
	@Mock
	private AuthenticationFacade authenticationFacadeMock;
	@Mock
	private UserAuthorizationValidator userAuthorizationValidatorMock;
	@Mock
	private NotificationService notificationServiceMock;
	@Mock
	private Authentication authenticationMock;
	@Mock
	private SecurityUser securityUserMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private ConnectionRepository connectionRepositoryMock;

	@Test
	public void createConnection_createsConnection() {
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(1L);
		when(connectionMock.getRequesterId()).thenReturn(2L);

		Connection createdConnection = connectionService.createConnection(connectionMock);

		verify(connectionValidatorMock).validateConnectionCreationArgument(1L, connectionMock);
		verify(userAuthorizationValidatorMock).validateUserRequest(2L, authenticationMock);
		verify(connectionDaoMock).save(connectionMock);
		verify(notificationServiceMock).createNotification(connectionMock);
	}

	@Test
	public void updateConnection_connectionNotFound_throwsException() {
		when(connectionDaoMock.get(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> connectionService.updateConnection(connectionMock, 1L));
	}

	@Test
	public void updateConnection_updatesConnection() {
		Long connectionId = 1L;
		Long userId = 1L;
		Long requesterId = 2L;
		Long requesteeId = 3L;
		when(connectionDaoMock.get(connectionId)).thenReturn(Optional.of(connectionMock));
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(userId);
		when(connectionMock.getRequesterId()).thenReturn(requesterId);
		when(connectionMock.getRequesteeId()).thenReturn(requesteeId);
		when(connectionMock.getStatus()).thenReturn(ConnectionStatus.ACCEPTED);

		Connection updatedConnection = connectionService.updateConnection(connectionMock, connectionId);

		assertEquals(connectionMock, updatedConnection);
		verify(connectionValidatorMock).validateConnectionUpdateArgument(userId, connectionMock, connectionMock, connectionId);
		verify(userAuthorizationValidatorMock).validateUserRequest(List.of(requesterId, requesteeId), authenticationMock);
		verify(connectionMock).merge(connectionMock);
		verify(connectionDaoMock).update(connectionMock);
		verify(notificationServiceMock).createNotification(connectionMock);
	}

	@Test
	public void updateConnection_statusIsNotAccepted_updatesConnectionAndDoesNotCreateNotification() {
		Long connectionId = 1L;
		Long userId = 1L;
		Long requesterId = 2L;
		Long requesteeId = 3L;
		when(connectionDaoMock.get(connectionId)).thenReturn(Optional.of(connectionMock));
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(userId);
		when(connectionMock.getRequesterId()).thenReturn(requesterId);
		when(connectionMock.getRequesteeId()).thenReturn(requesteeId);
		when(connectionMock.getStatus()).thenReturn(ConnectionStatus.REQUESTED);

		Connection updatedConnection = connectionService.updateConnection(connectionMock, connectionId);

		assertEquals(connectionMock, updatedConnection);
		verify(connectionValidatorMock).validateConnectionUpdateArgument(userId, connectionMock, connectionMock, connectionId);
		verify(userAuthorizationValidatorMock).validateUserRequest(List.of(requesterId, requesteeId), authenticationMock);
		verify(connectionMock).merge(connectionMock);
		verify(connectionDaoMock).update(connectionMock);
		verifyNoInteractions(notificationServiceMock);
	}

	@Test
	public void deleteConnection_connectionNotFound_throwsException() {
		when(connectionDaoMock.get(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> connectionService.deleteConnection(1L));
	}

	@Test
	public void deleteConnection_updatesConnection() {
		Long connectionId = 1L;
		Long requesterId = 2L;
		Long requesteeId = 3L;
		when(connectionDaoMock.get(connectionId)).thenReturn(Optional.of(connectionMock));
		when(connectionMock.getRequesteeId()).thenReturn(requesteeId);
		when(connectionMock.getRequesterId()).thenReturn(requesterId);
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		connectionService.deleteConnection(connectionId);

		verify(userAuthorizationValidatorMock).validateUserRequest(List.of(requesteeId, requesterId), authenticationMock);
		verify(connectionDaoMock).delete(connectionMock);
	}

	@Test
	public void deleteConnectionByConnectedUserId_noConnectionExists_throwsException() {
		Long userId = 1L;
		Long connectedUserId = 2L;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(userId);
		when(connectionRepositoryMock.findConnectionBetweenUsersWithAnyStatus(userId, connectedUserId))
				.thenReturn(Collections.emptyList());

		assertThrows(NotFoundException.class, () -> connectionService.deleteConnectionByConnectedUserId(connectedUserId));
	}

	@Test
	public void deleteConnectionByConnectedUserId_deletesConnection() {
		Long userId = 1L;
		Long connectedUserId = 2L;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(userId);
		List<Connection> connections = List.of(connectionMock);
		when(connectionRepositoryMock.findConnectionBetweenUsersWithAnyStatus(userId, connectedUserId))
				.thenReturn(connections);

		connectionService.deleteConnectionByConnectedUserId(connectedUserId);

		verify(connectionDaoMock).delete(connectionMock);
	}
}
