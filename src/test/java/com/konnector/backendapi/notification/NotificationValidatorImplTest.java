package com.konnector.backendapi.notification;

import com.konnector.backendapi.connection.Connection;
import com.konnector.backendapi.exceptions.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationValidatorImplTest {

	@InjectMocks
	private NotificationValidator notificationValidator = new NotificationValidatorImpl();

	@Mock
	private Connection connectionMock;
	@Mock
	private Notification notificationMock;

	@Test
	public void validateNotificationCreationConnectionArgument_argIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationCreationArgument((Connection) null));
	}

	@Test
	public void validateNotificationCreationConnectionArgument_argIsValid_doesNotThrowException() {
		notificationValidator.validateNotificationCreationArgument(connectionMock);
		verify(connectionMock, times(1)).validateForUpdate();
	}

	@Test
	public void validateNotificationCreationNotificationArgument_argIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationCreationArgument((Notification) null));
	}

	@Test
	public void validateNotificationCreationNotificationArgument_argIsValid_doesNotThrowException() {
		notificationValidator.validateNotificationCreationArgument(notificationMock);
		verify(notificationMock, times(1)).validateForCreation();
	}

	@Test
	public void validateNotificationFetchRequest_nullUserId_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationsFetchRequest(null, 1, 1));
	}

	@Test
	public void validateNotificationFetchRequest_nullPageNumber_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationsFetchRequest(1L, null, 1));
	}

	@Test
	public void validateNotificationFetchRequest_nullPageSize_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationsFetchRequest(1L, 1, null));
	}

	@Test
	public void validateNotificationFetchRequest_pageSizeTooBig_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationsFetchRequest(1L, 1, 11));
	}

	@Test
	public void validateNotificationFetchRequest_validArgs_doesNotThrowException() {
		notificationValidator.validateNotificationsFetchRequest(1L, 1, 1);
	}

	@Test
	public void validateNotificationCountFetchRequest_userIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationsCountFetchRequest(null));
	}

	@Test
	public void validateNotificationCountFetchRequest_argIsValid_doesNotThrowException() {
		notificationValidator.validateNotificationsCountFetchRequest(1L);
	}

	@Test
	public void validateNotificationDeleteRequest_notificationIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> notificationValidator.validateNotificationDeleteRequest(null));
	}

	@Test
	public void validateNotificationDeleteRequest_argIsValid_doesNotThrowException() {
		notificationValidator.validateNotificationDeleteRequest(1L);
	}
}
