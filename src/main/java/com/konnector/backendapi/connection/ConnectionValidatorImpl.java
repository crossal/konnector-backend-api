package com.konnector.backendapi.connection;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ConnectionValidatorImpl implements ConnectionValidator {

	private static final int MAX_PAGE_SIZE = 10;

	@Autowired
	private ConnectionRepository connectionRepository;

	@Override
	public void validateConnectionCreationArgument(Long userId, Connection connection) {
		if (connection == null) {
			throw new InvalidDataException("Connection cannot be empty.");
		}

		connection.validateForCreation();

		if (!userId.equals(connection.getRequesterId())) {
			throw new InvalidDataException("Requester Id must be logged in users.");
		}

		if (userId.equals(connection.getRequesteeId())) {
			throw new InvalidDataException("Requestee Id cannot be logged in users.");
		}

		Collection<Connection> connections = connectionRepository.findConnectionBetweenUsersWithAnyStatus(connection.getRequesteeId(), connection.getRequesterId());
		if (!connections.isEmpty()) {
			throw new InvalidDataException("Connection already exists.");
		}
	}

	@Override
	public void validateConnectionUpdateArgument(Long userId, Connection existingConnection, Connection connectionArg, Long connectionId) {
		if (connectionArg == null) {
			throw new InvalidDataException("Connection cannot be empty.");
		}

		if (connectionId == null) {
			throw new InvalidDataException("Connection Id cannot be empty.");
		}

		connectionArg.validateForUpdate();

		if (!connectionArg.getId().equals(connectionId)) {
			throw new InvalidDataException("Connection Id does not equal Id in path.");
		}

		if (!existingConnection.getRequesterId().equals(connectionArg.getRequesterId())) {
			throw new InvalidDataException("Cannot update requester Id.");
		}

		if (!existingConnection.getRequesteeId().equals(connectionArg.getRequesteeId())) {
			throw new InvalidDataException("Cannot update requestee Id.");
		}

		if (userId.equals(connectionArg.getRequesterId())
				&& !connectionArg.getStatus().equals(existingConnection.getStatus())) {
			throw new InvalidDataException("Requester cannot update status.");
		}

		if (userId.equals(connectionArg.getRequesteeId())
				&& !existingConnection.getStatus().equals(ConnectionStatus.REQUESTED)) {
			throw new InvalidDataException("Connection already resolved.");
		}
	}

	@Override
	public void validateConnectionDeleteRequest(Long userId, Connection connection) {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}

		if (connection == null) {
			throw new InvalidDataException("Connection cannot be null.");
		}
	}
}
