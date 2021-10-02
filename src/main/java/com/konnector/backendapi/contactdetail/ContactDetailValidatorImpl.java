package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.springframework.stereotype.Service;

@Service
public class ContactDetailValidatorImpl implements ContactDetailValidator {

	private static final int MAX_PAGE_SIZE = 10;

	@Override
	public void validateContactDetailCreationArgument(ContactDetail contactDetail) {
		if (contactDetail == null) {
			throw new InvalidDataException("Contact detail cannot be empty.");
		}

		contactDetail.validateForCreation();
	}

	@Override
	public void validateContactDetailUpdateArgument(ContactDetail existingContactDetail, ContactDetail contactDetailArg, Long contactDetailId) {
		if (contactDetailArg == null) {
			throw new InvalidDataException("Contact detail cannot be empty.");
		}

		if (contactDetailId == null) {
			throw new InvalidDataException("Contact detail Id cannot be empty.");
		}

		contactDetailArg.validateForUpdate();

		if (!contactDetailArg.getId().equals(contactDetailId)) {
			throw new InvalidDataException("Contact detail Id does not equal Id in path.");
		}

		if (!existingContactDetail.getUserId().equals(contactDetailArg.getUserId())) {
			throw new InvalidDataException("Cannot update user Id.");
		}
	}

	@Override
	public void validateContactDetailsFetchRequest(Long userId, Integer pageNumber, Integer pageSize) {
		if (userId == null) {
			throw new InvalidDataException("User Id cannot be empty.");
		}

		if (pageNumber == null) {
			throw new InvalidDataException("Page number cannot be empty.");
		}

		if (pageSize == null) {
			throw new InvalidDataException("Page size cannot be empty.");
		}

		if (pageSize > MAX_PAGE_SIZE) {
			throw new InvalidDataException("Page size cannot be larger than " + MAX_PAGE_SIZE + ".");
		}
	}
}
