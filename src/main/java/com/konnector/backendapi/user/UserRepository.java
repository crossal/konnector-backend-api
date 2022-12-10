package com.konnector.backendapi.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
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
			"   AND (c.requesterId = ?1 OR c.requesteeId = ?1) " +
			"   AND connected_u.emailVerified = 1 " +
			"   AND connected_u.username LIKE CONCAT(?2,'%') " +
			"ORDER BY connected_u.username ASC, connected_u.firstName ASC, connected_u.lastName ASC")
	Page getConnections(Long userId, String username, Pageable pageable);

	@Query("" +
			"SELECT count(c) FROM Connection c " +
			"INNER JOIN User connected_u ON connected_u.id = " +
			"  CASE " +
			"    WHEN c.requesterId = ?1 THEN c.requesteeId " +
			"    ELSE c.requesterId " +
			"  END " +
			"WHERE c.status = 1 " +
			"   AND (c.requesterId = ?1 OR c.requesteeId = ?1) " +
			"   AND connected_u.emailVerified = 1 " +
			"   AND connected_u.username LIKE CONCAT(?2,'%')")
	long countConnectionsByUserId(Long userId, String username);

	@Query("" +
			"SELECT u FROM User u " +
			"LEFT JOIN Connection c ON ((c.requesterId = u.id OR c.requesteeId = u.id) AND (c.requesterId = ?1 OR c.requesteeId = ?1)) " +
			"WHERE u.id != ?1 " +
			"   AND c.id IS NULL " +
			"   AND u.emailVerified = 1 " +
			"   AND u.username LIKE CONCAT(?2,'%') " +
			"ORDER BY u.username ASC, u.firstName ASC, u.lastName ASC")
	Page getNonConnections(Long userId, String username, Pageable pageable);

	@Query("" +
			"SELECT count(u) FROM User u " +
			"LEFT JOIN Connection c ON ((c.requesterId = u.id OR c.requesteeId = u.id) AND (c.requesterId = ?1 OR c.requesteeId = ?1)) " +
			"WHERE u.id != ?1 " +
			"   AND c.id IS NULL " +
			"   AND u.emailVerified = 1 " +
			"   AND u.username LIKE CONCAT(?2,'%')")
	long countNonConnectionsByUserId(Long userId, String username);

	Page findByIdNotAndEmailVerifiedTrueAndUsernameStartingWith(Long id,  String username, Pageable pageable);
	long countByIdNotAndEmailVerifiedTrueAndUsernameStartingWith(Long id, String username);
}
