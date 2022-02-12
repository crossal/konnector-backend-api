package com.konnector.backendapi.user;

import com.konnector.backendapi.data.Dao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class JpaUserDao implements Dao<User> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<User> get(long id) {
		return Optional.ofNullable(entityManager.find(User.class, id));
	}

	@Override
	public void save(User user) {
		entityManager.persist(user);
	}

	@Override
	public void update(User user) {
		entityManager.merge(user);
	}

	@Override
	public void delete(User user) {
		entityManager.remove(user);
	}

	@Override
	public void flush() {
		entityManager.flush();
	}
}
