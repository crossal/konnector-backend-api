package com.konnector.backendapi.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByEmailOrUsername(String email, String username);

	@Query("" +
			"SELECT connected_u FROM Connection c " +
			"INNER JOIN User connected_u ON connected_u.id = " +
			"  CASE " +
			"    WHEN c.requesterId = ?1 THEN c.requesteeId " +
			"    ELSE c.requesterId " +
			"  END " +
			"WHERE c.status = 1 " +
			"AND (c.requesterId = ?1 OR c.requesteeId = ?1) " +
			"ORDER BY connected_u.username ASC")
	Page getConnections(Long userId, Pageable pageable);

	@Query("" +
			"SELECT count(c) FROM Connection c " +
			"WHERE c.status = 1 " +
			"AND (c.requesterId = ?1 OR c.requesteeId = ?1)")
	long countConnectionsByUserId(Long userId);

	Page findByIdNot(Long id, Pageable pageable);
	long countByIdNot(Long id);
}
