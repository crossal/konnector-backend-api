package com.konnector.backendapi.notification;

import com.konnector.backendapi.connection.Connection;

public interface NotificationValidator {
	void validateNotificationCreationArgument(Connection connection);
	void validateNotificationCreationArgument(Notification notification);
	void validateNotificationsFetchRequest(Long userId, Integer pageNumber, Integer pageSize);
	void validateNotificationsCountFetchRequest(Long userId);
	void validateNotificationDeleteRequest(Long notificationId);
}
