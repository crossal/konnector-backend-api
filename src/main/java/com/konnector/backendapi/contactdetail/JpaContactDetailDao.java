package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.data.Dao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class JpaContactDetailDao implements Dao<ContactDetail> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<ContactDetail> get(long id) {
		return Optional.ofNullable(entityManager.find(ContactDetail.class, id));
	}

	@Override
	public void save(ContactDetail contactDetail) {
		entityManager.persist(contactDetail);
	}

	@Override
	public void update(ContactDetail contactDetail) {
		entityManager.merge(contactDetail);
	}

	@Override
	public void delete(ContactDetail contactDetail) {
		entityManager.remove(contactDetail);
	}
}
