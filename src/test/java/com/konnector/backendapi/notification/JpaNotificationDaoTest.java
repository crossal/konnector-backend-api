package com.konnector.backendapi.notification;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaNotificationDaoTest {

	@InjectMocks
	private final JpaNotificationDao jpaNotificationDao = new JpaNotificationDao();

	@Mock
	private EntityManager entityManagerMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final Notification notification = easyRandom.nextObject(Notification.class);

	@Test
	public void get_returnsNotification() {
		long notificationId = 1;
		when(entityManagerMock.find(Notification.class, notificationId)).thenReturn(notification);
		assertEquals(notification, jpaNotificationDao.get(notificationId).get());
	}

	@Test
	public void save_savesNotification() {
		jpaNotificationDao.save(notification);

		verify(entityManagerMock).persist(notification);
	}

	@Test
	public void update_updatesNotification() {
		jpaNotificationDao.update(notification);

		verify(entityManagerMock).merge(notification);
	}

	@Test
	public void delete_deletesNotification() {
		jpaNotificationDao.delete(notification);

		verify(entityManagerMock).remove(notification);
	}
}
