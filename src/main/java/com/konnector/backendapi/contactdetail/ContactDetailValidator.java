package com.konnector.backendapi.contactdetail;

public interface ContactDetailValidator {
	void validateContactDetailCreationArgument(ContactDetail contactDetail);
	void validateContactDetailUpdateArgument(ContactDetail existingContactDetail, ContactDetail contactDetailArg, Long contactDetailId);
	void validateContactDetailsFetchRequest(Long userId, Integer pageNumber, Integer pageSize);
}
