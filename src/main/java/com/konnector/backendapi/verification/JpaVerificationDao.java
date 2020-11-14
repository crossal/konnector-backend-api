package com.konnector.backendapi.verification;

import com.konnector.backendapi.data.Dao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class JpaVerificationDao implements Dao<Verification> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<Verification> get(long id) {
		return Optional.ofNullable(entityManager.find(Verification.class, id));
	}

//	@Override
//	public List<User> getAll() {
//		Query query = entityManager.createQuery("SELECT e FROM User e");
//		return query.getResultList();
//	}

	@Override
	public void save(Verification verification) {
//		executeInsideTransaction(entityManager -> entityManager.persist(user));
		entityManager.persist(verification);
	}

	@Override
	public void update(Verification verification) {
//		user.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
//		user.setEmail(Objects.requireNonNull(params[1], "Email cannot be null"));
//		executeInsideTransaction(entityManager -> entityManager.merge(user));
		entityManager.merge(verification);
	}

//	@Override
//	public void delete(User user) {
//		executeInsideTransaction(entityManager -> entityManager.remove(user));
//	}

	@Override
	public void delete(Verification verification) {
		entityManager.remove(verification);
	}

//	private void executeInsideTransaction(Consumer<EntityManager> action) {
//		EntityTransaction tx = entityManager.getTransaction();
//		try {
//			tx.begin();
//			action.accept(entityManager);
//			tx.commit();
//		}
//		catch (RuntimeException e) {
//			tx.rollback();
//			throw e;
//		}
//	}
}
