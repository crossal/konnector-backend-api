package com.konnector.backendapi.notification;

import com.konnector.backendapi.connection.Connection;
import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.stereotype.Service;

@Service
public class NotificationValidatorImpl implements NotificationValidator {

	private static final int MAX_PAGE_SIZE = 10;

	@Override
	public void validateNotificationCreationArgument(Connection connection) {
		if (connection == null) {
			throw new InvalidDataException("Connection cannot be empty.");
		}

		connection.validateForUpdate();
	}

	@Override
	public void validateNotificationCreationArgument(Notification notification) {
		if (notification == null) {
			throw new InvalidDataException("Notification cannot be empty.");
		}

		notification.validateForCreation();
	}

	@Override
	public void validateNotificationsFetchRequest(Long userId, Integer pageNumber, Integer pageSize) {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}

		if (pageNumber == null) {
			throw new InvalidDataException("Page number cannot be empty.");
		}

		if (pageSize == null) {
			throw new InvalidDataException("Page size cannot be empty.");
		}

		if (pageSize > MAX_PAGE_SIZE) {
			throw new InvalidDataException("Page size cannot be larger than " + MAX_PAGE_SIZE + ".");
		}
	}

	@Override
	public void validateNotificationsCountFetchRequest(Long userId) {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}
	}

	@Override
	public void validateNotificationDeleteRequest(Long notificationId) {
		if (notificationId == null) {
			throw new InvalidDataException("Notification Id cannot be empty.");
		}
	}
}
