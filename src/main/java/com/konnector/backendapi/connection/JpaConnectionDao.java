package com.konnector.backendapi.connection;

import com.konnector.backendapi.data.Dao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class JpaConnectionDao implements Dao<Connection> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<Connection> get(long id) {
		return Optional.ofNullable(entityManager.find(Connection.class, id));
	}

	@Override
	public void save(Connection connection) {
		entityManager.persist(connection);
	}

	@Override
	public void update(Connection connection) {
		entityManager.merge(connection);
	}

	@Override
	public void delete(Connection connection) {
		entityManager.remove(connection);
	}

	@Override
	public void flush() {
		entityManager.flush();
	}
}
