package com.konnector.backendapi.contactdetail;

import java.util.List;

public interface ContactDetailService {
	ContactDetail createContactDetail(ContactDetail contactDetail);
	ContactDetail updateContactDetail(ContactDetail contactDetail, Long contactDetailId);
	List<ContactDetail> getContactDetails(Long userId, Integer pageNumber, Integer pageSize);
	long getContactDetailsCount(Long userId);
	void deleteContactDetail(Long id);
}
