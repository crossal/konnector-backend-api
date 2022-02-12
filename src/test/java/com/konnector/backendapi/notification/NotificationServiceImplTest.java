package com.konnector.backendapi.notification;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.connection.Connection;
import com.konnector.backendapi.connection.ConnectionStatus;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.user.UserAuthorizationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

	@InjectMocks
	private NotificationService notificationService = new NotificationServiceImpl();

	@Mock
	private Dao<Notification> notificationDaoMock;
	@Mock
	private NotificationValidator notificationValidatorMock;
	@Mock
	private AuthenticationFacade authenticationFacadeMock;
	@Mock
	private UserAuthorizationValidator userAuthorizationValidatorMock;
	@Mock
	private NotificationRepository notificationRepository;
	@Mock
	private Authentication authenticationMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private Notification notificationMock;
	@Mock
	private Page pageMock;
//	@Mock
//	private User userMock;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	@Test
	public void createNotification_statusIsRequested_createsNotification() {
		Long requesterId = 1L;
		Long requesteeId = 2L;
		when(connectionMock.getStatus()).thenReturn(ConnectionStatus.REQUESTED);
		when(connectionMock.getRequesterId()).thenReturn(requesterId);
		when(connectionMock.getRequesteeId()).thenReturn(requesteeId);

		Notification createdNotification = notificationService.createNotification(connectionMock);

		assertEquals(requesteeId, createdNotification.getRecipientId());
		assertEquals(requesterId, createdNotification.getSenderId());
		assertEquals(NotificationType.CONNECTION_REQUEST, createdNotification.getType());
		assertEquals(requesterId, createdNotification.getReferenceId());
		verify(notificationValidatorMock).validateNotificationCreationArgument(connectionMock);
		verify(notificationDaoMock).save(createdNotification);
	}

	@Test
	public void createNotification_statusIsNotRequested_createsNotification() {
		Long requesterId = 1L;
		Long requesteeId = 2L;
		when(connectionMock.getStatus()).thenReturn(ConnectionStatus.ACCEPTED);
		when(connectionMock.getRequesterId()).thenReturn(requesterId);
		when(connectionMock.getRequesteeId()).thenReturn(requesteeId);

		Notification createdNotification = notificationService.createNotification(connectionMock);

		assertEquals(requesterId, createdNotification.getRecipientId());
		assertEquals(requesteeId, createdNotification.getSenderId());
		assertEquals(NotificationType.CONNECTION_ACCEPT, createdNotification.getType());
		assertEquals(requesteeId, createdNotification.getReferenceId());
		verify(notificationValidatorMock).validateNotificationCreationArgument(connectionMock);
		verify(notificationDaoMock).save(createdNotification);
	}

	@Test
	public void getNotifications_getsNotifications() {
		Long userId = 1L;
		Integer pageNumber = 2;
		Integer pageSize = 5;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		List<Notification> notifications = List.of(notificationMock);
		when(notificationRepository.findByRecipientId(eq(userId), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(notifications);

		List<Notification> returnedNotifications = notificationService.getNotifications(userId, pageNumber, pageSize);

		assertEquals(notifications, returnedNotifications);
		verify(notificationValidatorMock).validateNotificationsFetchRequest(userId, pageNumber, pageSize);
		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(notificationRepository).findByRecipientId(eq(userId), pageableCaptor.capture());
		Pageable pageable = pageableCaptor.getValue();
		assertEquals(pageNumber - 1, pageable.getPageNumber());
		assertEquals(pageSize, pageable.getPageSize());
		Sort.Order order = pageable.getSort().stream().iterator().next();
		assertEquals("createdOn", order.getProperty());
		assertEquals(Sort.Direction.DESC, order.getDirection());
	}

	@Test
	public void getNotificationsCount_getsNotificationsCount() {
		long count = 10L;
		Long userId = 1L;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		when(notificationRepository.countByRecipientId(userId)).thenReturn(count);

		long returnedNotificationsCount = notificationService.getNotificationsCount(userId);

		assertEquals(count, returnedNotificationsCount);
		verify(notificationValidatorMock).validateNotificationsCountFetchRequest(userId);
		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(notificationRepository).countByRecipientId(userId);
	}

	@Test
	public void deleteNotification_notificationNotFound_throwsException() {
		when(notificationDaoMock.get(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> notificationService.deleteNotification(1L));
	}

	@Test
	public void deleteNotification_updatesNotification() {
		Long notificationId = 1L;
		Long recipientId = 2L;
		when(notificationDaoMock.get(notificationId)).thenReturn(Optional.of(notificationMock));
		when(notificationMock.getRecipientId()).thenReturn(recipientId);
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		notificationService.deleteNotification(notificationId);

		verify(userAuthorizationValidatorMock).validateUserRequest(recipientId, authenticationMock);
		verify(notificationDaoMock).delete(notificationMock);
	}
}
