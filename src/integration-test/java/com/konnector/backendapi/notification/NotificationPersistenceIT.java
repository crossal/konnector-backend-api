package com.konnector.backendapi.notification;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.jeasy.random.FieldPredicates.named;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/notification/notification-insert-data.sql" })
@Import(JpaNotificationDao.class)
public class NotificationPersistenceIT {

	@Autowired
	private JpaNotificationDao jpaNotificationDao;

	private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters().excludeField(named("id")));
	private final Notification notification = easyRandom.nextObject(Notification.class);

	@Test
	@Transactional
	public void save_savesNotification() {
		notification.setSenderId(1L);
		notification.setRecipientId(4L);
		notification.setReferenceId(1L);
		jpaNotificationDao.save(notification);
		assertEquals(notification, jpaNotificationDao.get(notification.getId()).get());
	}

	@Test
	@Transactional
	public void update_updatesNotification() {
		Notification notification = jpaNotificationDao.get(1L).get();
		notification.setType(NotificationType.CONNECTION_ACCEPT);
		jpaNotificationDao.update(notification);
		jpaNotificationDao.flush();
		Notification updatedNotification = jpaNotificationDao.get(notification.getId()).get();
		assertEquals(notification.getSenderId(), updatedNotification.getSenderId());
	}

	@Test
	@Transactional
	public void get_getsNotification() {
		assertTrue(jpaNotificationDao.get(1L).isPresent());
	}

	@Test
	@Transactional
	public void delete_deletesNotification() {
		Notification notification = jpaNotificationDao.get(1L).get();
		jpaNotificationDao.delete(notification);
		assertTrue(jpaNotificationDao.get(1L).isEmpty());
	}
}
