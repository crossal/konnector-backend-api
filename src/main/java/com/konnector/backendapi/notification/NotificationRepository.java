package com.konnector.backendapi.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository  extends JpaRepository<Notification, Long> {
	Page findByRecipientId(Long id, Pageable pageable);
	@Query(value =
		"SELECT n FROM Notification n " +
		"JOIN FETCH n.recipient " +
		"JOIN FETCH n.sender " +
		"WHERE n.recipientId = ?1 " +
		"ORDER BY n.updatedOn DESC",
		countQuery =
		"SELECT COUNT(n) FROM Notification n WHERE n.recipientId = ?1")
	Page findByRecipientIdAndLoadRecipientAndSender(Long id, Pageable pageable);
	long countByRecipientId(Long id);
}
