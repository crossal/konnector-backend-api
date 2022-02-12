package com.konnector.backendapi.notification;

import com.konnector.backendapi.data.Dao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class JpaNotificationDao  implements Dao<Notification> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<Notification> get(long id) {
		return Optional.ofNullable(entityManager.find(Notification.class, id));
	}

	@Override
	public void save(Notification notification) {
		entityManager.persist(notification);
	}

	@Override
	public void update(Notification notification) {
		entityManager.merge(notification);
	}

	@Override
	public void delete(Notification notification) {
		entityManager.remove(notification);
	}

	@Override
	public void flush() {
		entityManager.flush();
	}
}
