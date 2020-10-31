package com.konnector.backendapi.user;

import com.konnector.backendapi.data.Dao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class JpaUserDao implements Dao<User> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<User> get(long id) {
		return Optional.ofNullable(entityManager.find(User.class, id));
	}

//	@Override
//	public List<User> getAll() {
//		Query query = entityManager.createQuery("SELECT e FROM User e");
//		return query.getResultList();
//	}

	@Override
	public void save(User user) {
//		executeInsideTransaction(entityManager -> entityManager.persist(user));
		entityManager.persist(user);
	}

	@Override
	public void update(User user, String[] params) {
//		user.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
//		user.setEmail(Objects.requireNonNull(params[1], "Email cannot be null"));
//		executeInsideTransaction(entityManager -> entityManager.merge(user));
		entityManager.merge(user);
	}

	@Override
	public void delete(User user) {
		executeInsideTransaction(entityManager -> entityManager.remove(user));
	}

	private void executeInsideTransaction(Consumer<EntityManager> action) {
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			action.accept(entityManager);
			tx.commit();
		}
		catch (RuntimeException e) {
			tx.rollback();
			throw e;
		}
	}
}
