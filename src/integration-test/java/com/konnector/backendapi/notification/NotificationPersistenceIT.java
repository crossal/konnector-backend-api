package com.konnector.backendapi.notification;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.jeasy.random.FieldPredicates.named;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/notification/notification-insert-data.sql" })
public class NotificationPersistenceIT {

	@Autowired
	private NotificationRepository notificationRepository;

	private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters().excludeField(named("id")));
	private final Notification notification = easyRandom.nextObject(Notification.class);

	@Test
	@Transactional
	public void save_savesNotification() {
		notification.setSenderId(1L);
		notification.setRecipientId(4L);
		notification.setReferenceId(1L);
		notificationRepository.save(notification);
		assertEquals(notification, notificationRepository.findById(notification.getId()).get());
	}

	@Test
	@Transactional
	public void update_updatesNotification() {
		Notification notification = notificationRepository.findById(1L).get();
		notification.setType(NotificationType.CONNECTION_ACCEPT);
		notificationRepository.save(notification);
		Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
		assertEquals(notification.getSenderId(), updatedNotification.getSenderId());
	}

	@Test
	@Transactional
	public void get_getsNotification() {
		assertTrue(notificationRepository.findById(1L).isPresent());
	}

	@Test
	@Transactional
	public void delete_deletesNotification() {
		Notification notification = notificationRepository.findById(1L).get();
		notificationRepository.delete(notification);
		assertTrue(notificationRepository.findById(1L).isEmpty());
	}
}
