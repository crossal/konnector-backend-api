package com.konnector.backendapi.contactdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactDetailRepository extends JpaRepository<ContactDetail, Long> {
	Page<ContactDetail> findByUserId(Long userId, Pageable pageable);
	long countByUserId(Long userId);
}
