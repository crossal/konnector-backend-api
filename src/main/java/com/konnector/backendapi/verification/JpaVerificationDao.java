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

	@Override
	public void save(Verification verification) {
		entityManager.persist(verification);
	}

	@Override
	public void update(Verification verification) {
		entityManager.merge(verification);
	}

	@Override
	public void delete(Verification verification) {
		entityManager.remove(verification);
	}
}
