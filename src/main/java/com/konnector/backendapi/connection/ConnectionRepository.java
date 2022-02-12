package com.konnector.backendapi.connection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

public interface ConnectionRepository extends PagingAndSortingRepository<Connection, Long> {
	@Query("" +
			"SELECT c FROM Connection c " +
			"WHERE (c.requesterId = ?1 OR c.requesteeId = ?2) AND (c.requesterId = ?2 OR c.requesteeId = ?1)")
	Collection<Connection> findConnectionBetweenUsersWithAnyStatus(Long userIdA, Long userIdB);
}
