package com.konnector.backendapi.connection;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionValidatorImplTest {

	@InjectMocks
	private ConnectionValidator connectionValidator = new ConnectionValidatorImpl();

	@Mock
	private ConnectionRepository connectionRepositoryMock;

	@Mock
	private Connection connectionMock;
	@Mock
	private Connection existingConnectionMock;

	@Test
	public void validateConnectionCreationArgument_connectionIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionCreationArgument(1L, null));
	}

	@Test
	public void validateConnectionCreationArgument_userIdDoesNotEqualRequestertId_throwsException() {
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionCreationArgument(1L, connectionMock));
	}

	@Test
	public void validateConnectionCreationArgument_userIdEqualsRequesteeId_throwsException() {
		when(connectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesteeId()).thenReturn(1L);
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionCreationArgument(1L, connectionMock));
	}

	@Test
	public void validateConnectionCreationArgument_connectionAlreadyExists_throwsException() {
		when(connectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesteeId()).thenReturn(2L);
		when(connectionRepositoryMock.findConnectionBetweenUsersWithAnyStatus(2L, 1L)).thenReturn(List.of(connectionMock));
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionCreationArgument(1L, connectionMock));
	}

	@Test
	public void validateConnectionCreationArgument_argumentIsValid_doesNotThrowException() {
		when(connectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesteeId()).thenReturn(2L);
		when(connectionRepositoryMock.findConnectionBetweenUsersWithAnyStatus(2L, 1L)).thenReturn(Collections.emptyList());
		connectionValidator.validateConnectionCreationArgument(1L, connectionMock);
		verify(connectionMock).validateForCreation();
	}

	@Test
	public void validateConnectionUpdateArgument_connectionIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionUpdateArgument(1L, existingConnectionMock, null, 1L));
	}

	@Test
	public void validateConnectionUpdateArgument_connectionIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionUpdateArgument(1L, existingConnectionMock, connectionMock, null));
	}

	@Test
	public void validateConnectionUpdateArgument_connectionsIdDoesNotEqualConnectionId_throwsException() {
		when(connectionMock.getId()).thenReturn(2L);
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionUpdateArgument(1L, existingConnectionMock, connectionMock, 1L));
	}

	@Test
	public void validateConnectionUpdateArgument_existingConnectionRequesterIdDoesNotEqualConnectionRequesterId_throwsException() {
		when(connectionMock.getId()).thenReturn(1L);
		when(existingConnectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesterId()).thenReturn(2L);
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionUpdateArgument(1L, existingConnectionMock, connectionMock, 1L));
	}

	@Test
	public void validateConnectionUpdateArgument_existingConnectionRequesteeIdDoesNotEqualConnectionRequesteeId_throwsException() {
		when(connectionMock.getId()).thenReturn(1L);
		when(existingConnectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesterId()).thenReturn(1L);
		when(existingConnectionMock.getRequesteeId()).thenReturn(2L);
		when(connectionMock.getRequesteeId()).thenReturn(3L);
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionUpdateArgument(1L, existingConnectionMock, connectionMock, 1L));
	}

	@Test
	public void validateConnectionUpdateArgument_userIdEqualsConnectionRequesterIdAndStatusIsNew_throwsException() {
		when(connectionMock.getId()).thenReturn(1L);
		when(existingConnectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesterId()).thenReturn(1L);
		when(existingConnectionMock.getRequesteeId()).thenReturn(2L);
		when(connectionMock.getRequesteeId()).thenReturn(2L);
		when(existingConnectionMock.getStatus()).thenReturn(ConnectionStatus.REQUESTED);
		when(connectionMock.getStatus()).thenReturn(ConnectionStatus.ACCEPTED);
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionUpdateArgument(1L, existingConnectionMock, connectionMock, 1L));
	}

	@Test
	public void validateConnectionUpdateArgument_userIdEqualsConnectionRequesteeIdAndExistingConnectionIsAlreadyResolved_throwsException() {
		when(connectionMock.getId()).thenReturn(1L);
		when(existingConnectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesterId()).thenReturn(1L);
		when(existingConnectionMock.getRequesteeId()).thenReturn(2L);
		when(connectionMock.getRequesteeId()).thenReturn(2L);
		when(existingConnectionMock.getStatus()).thenReturn(ConnectionStatus.ACCEPTED);
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionUpdateArgument(2L, existingConnectionMock, connectionMock, 1L));
	}

	@Test
	public void validateConnectionUpdateArgument_validArguments_doesNotThrowException() {
		when(connectionMock.getId()).thenReturn(1L);
		when(existingConnectionMock.getRequesterId()).thenReturn(1L);
		when(connectionMock.getRequesterId()).thenReturn(1L);
		when(existingConnectionMock.getRequesteeId()).thenReturn(2L);
		when(connectionMock.getRequesteeId()).thenReturn(2L);
		when(existingConnectionMock.getStatus()).thenReturn(ConnectionStatus.REQUESTED);
		connectionValidator.validateConnectionUpdateArgument(2L, existingConnectionMock, connectionMock, 1L);
		verify(connectionMock).validateForUpdate();
	}

	@Test
	public void validateConnectionDeleteRequest_emptyUserId_throwsException() {
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionDeleteRequest(null, connectionMock));
	}

	@Test
	public void validateConnectionDeleteRequest_emptyConnection_throwsException() {
		assertThrows(InvalidDataException.class, () -> connectionValidator.validateConnectionDeleteRequest(1L, null));
	}

	@Test
	public void validateConnectionDeleteRequest_validArguments_doesNotThrowException() {
		connectionValidator.validateConnectionDeleteRequest(1L, connectionMock);
	}
}
