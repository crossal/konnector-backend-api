package com.konnector.backendapi.connection;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.notification.NotificationService;
import com.konnector.backendapi.security.AuthenticationUtil;
import com.konnector.backendapi.user.UserAuthorizationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {

	@Autowired
	private Dao<Connection> connectionDao;
	@Autowired
	private ConnectionValidator connectionValidator;
	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private UserAuthorizationValidator userAuthorizationValidator;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ConnectionRepository connectionRepository;

	@Override
	@Transactional
	public Connection createConnection(Connection connection) {
		Authentication authentication = authenticationFacade.getAuthentication();

		connectionValidator.validateConnectionCreationArgument(AuthenticationUtil.getUserId(authentication), connection);
		userAuthorizationValidator.validateUserRequest(connection.getRequesterId(), authentication);

		connectionDao.save(connection);

		notificationService.createNotification(connection);

		return connection;
	}

	@Override
	@Transactional
	public Connection updateConnection(Connection connection, Long connectionId) {
		Optional<Connection> optionalConnection = connectionDao.get(connectionId);

		return optionalConnection.map(
				existingConnection -> {
					Authentication authentication = authenticationFacade.getAuthentication();

					connectionValidator.validateConnectionUpdateArgument(AuthenticationUtil.getUserId(authentication), existingConnection, connection, connectionId);
					userAuthorizationValidator.validateUserRequest(List.of(connection.getRequesterId(), connection.getRequesteeId()), authentication);

					existingConnection.merge(connection);

					connectionDao.update(existingConnection);

					if (existingConnection.getStatus().equals(ConnectionStatus.ACCEPTED)) {
						notificationService.createNotification(existingConnection);
					}

					return existingConnection;
				}
		).orElseThrow(() -> new NotFoundException("Connection not found."));
	}

	@Override
	@Transactional
	public void deleteConnection(Long id) {
		Optional<Connection> optionalConnection = connectionDao.get(id);

		optionalConnection.ifPresentOrElse(
				existingConnection -> {
					List<Long> userIds = List.of(existingConnection.getRequesteeId(),
							existingConnection.getRequesterId());

					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(userIds, authentication);

					connectionDao.delete(existingConnection);
				},
				() -> {
					throw new NotFoundException("Connection not found.");
				}
		);
	}

	@Override
	@Transactional
	public void deleteConnectionByConnectedUserId(Long connectedUserId) {
		Authentication authentication = authenticationFacade.getAuthentication();
		Long userId = AuthenticationUtil.getUserId(authentication);
		Collection<Connection> connections = connectionRepository.findConnectionBetweenUsersWithAnyStatus(userId, connectedUserId);

		if (connections.isEmpty()) {
			throw new NotFoundException("Connection not found.");
		}

		Connection connection = connections.stream().iterator().next();
		connectionDao.delete(connection);
	}
}
