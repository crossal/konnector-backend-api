package com.konnector.backendapi.connection;

public interface ConnectionService {
	Connection createConnection(Connection connection);
	Connection updateConnection(Connection connection, Long connectionId);
	void deleteConnection(Long id);
	void deleteConnectionByConnectedUserId(Long connectedUserId);
}
