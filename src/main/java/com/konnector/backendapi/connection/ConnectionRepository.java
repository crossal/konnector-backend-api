package com.konnector.backendapi.connection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface ConnectionRepository extends PagingAndSortingRepository<Connection, Long> {
	@Query("" +
			"SELECT c FROM Connection c " +
			"WHERE (c.requesterId = ?1 AND c.requesteeId = ?2) OR (c.requesterId = ?2 AND c.requesteeId = ?1)")
	Collection<Connection> findConnectionBetweenUsersWithAnyStatus(Long userIdA, Long userIdB);
	@Query("" +
			"SELECT c FROM Connection c " +
			"WHERE ((c.requesterId = ?1 AND c.requesteeId = ?2) OR (c.requesterId = ?2 AND c.requesteeId = ?1))" +
			"   AND c.status = ?3")
	Collection<Connection> findConnectionBetweenUsersWithStatus(Long userIdA, Long userIdB, ConnectionStatus status);
}
