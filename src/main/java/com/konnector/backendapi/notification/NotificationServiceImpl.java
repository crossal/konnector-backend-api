package com.konnector.backendapi.notification;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.connection.Connection;
import com.konnector.backendapi.connection.ConnectionStatus;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.user.UserAuthorizationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private Dao<Notification> notificationDao;
	@Autowired
	private NotificationValidator notificationValidator;
	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private UserAuthorizationValidator userAuthorizationValidator;
	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	@Transactional
	public Notification createNotification(Connection connection) {
		notificationValidator.validateNotificationCreationArgument(connection);

		NotificationType notificationType;
		Long senderId, recipientId, referenceId;
		if (connection.getStatus().equals(ConnectionStatus.REQUESTED)) {
			notificationType = NotificationType.CONNECTION_REQUEST;
			senderId = connection.getRequesterId();
			recipientId = connection.getRequesteeId();
			referenceId = connection.getRequesterId();
		} else {
			notificationType = NotificationType.CONNECTION_ACCEPT;
			senderId = connection.getRequesteeId();
			recipientId = connection.getRequesterId();
			referenceId = connection.getRequesteeId();
		}

		Notification notification = new Notification(recipientId, senderId, notificationType, referenceId);

		notificationDao.save(notification);

		return notification;
	}

	@Override
	@Transactional
	public List<Notification> getNotifications(Long userId, Integer pageNumber, Integer pageSize) {
		notificationValidator.validateNotificationsFetchRequest(userId, pageNumber, pageSize);

		Authentication authentication = authenticationFacade.getAuthentication();
		userAuthorizationValidator.validateUserRequest(userId, authentication);

		Pageable sortedByCreationDateDescPageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("createdOn").descending());
		Page page = notificationRepository.findByRecipientId(userId, sortedByCreationDateDescPageable);

		return page.getContent();
	}

	@Override
	@Transactional
	public long getNotificationsCount(Long userId) {
		notificationValidator.validateNotificationsCountFetchRequest(userId);

		Authentication authentication = authenticationFacade.getAuthentication();
		userAuthorizationValidator.validateUserRequest(userId, authentication);

		return notificationRepository.countByRecipientId(userId);
	}

	@Override
	@Transactional
	public void deleteNotification(Long id) {
		Optional<Notification> optionalNotification = notificationDao.get(id);

		optionalNotification.ifPresentOrElse(
				existingNotification -> {
					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(existingNotification.getRecipientId(), authentication);

					notificationDao.delete(existingNotification);
				},
				() -> {
					throw new NotFoundException("Notification not found.");
				}
		);
	}
}
