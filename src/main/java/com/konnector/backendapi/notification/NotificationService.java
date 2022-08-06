package com.konnector.backendapi.notification;

import com.konnector.backendapi.connection.Connection;

import java.util.List;

public interface NotificationService {
	Notification createNotification(Connection connection);
	List<Notification> getNotifications(Long userId, Integer pageNumber, Integer pageSize);
	long getNotificationsCount(Long userId);
	void deleteNotification(Long id);
}
