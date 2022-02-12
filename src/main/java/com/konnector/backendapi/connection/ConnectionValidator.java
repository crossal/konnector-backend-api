package com.konnector.backendapi.connection;

public interface ConnectionValidator {
	void validateConnectionCreationArgument(Long userId, Connection connection);
	void validateConnectionUpdateArgument(Long userId, Connection existingConnection, Connection connectionArg, Long connectionId);
	void validateConnectionDeleteRequest(Long userId, Connection connection);
}
