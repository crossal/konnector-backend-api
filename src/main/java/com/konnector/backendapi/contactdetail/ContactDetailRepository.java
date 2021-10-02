package com.konnector.backendapi.contactdetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContactDetailRepository extends PagingAndSortingRepository<ContactDetail, Long> {
	Page findByUserId(Long userId, Pageable pageable);
}
