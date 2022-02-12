package com.konnector.backendapi.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NotificationRepository  extends PagingAndSortingRepository<Notification, Long> {
	Page findByRecipientId(Long id, Pageable pageable);
	long countByRecipientId(Long id);
}
