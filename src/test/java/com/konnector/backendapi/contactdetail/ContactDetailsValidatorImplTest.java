package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.exceptions.InvalidDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactDetailsValidatorImplTest {

	@InjectMocks
	private ContactDetailValidator contactDetailValidator = new ContactDetailValidatorImpl();

	@Mock
	private ContactDetail contactDetailMock;
	@Mock
	private ContactDetail existingContactDetailMock;

	private static final Long contactDetailId = 1L;
	private static final Long userId = 1L;

	@Test
	public void validateContactDetailCreationArgument_contactDetailIsValid_doesNotThrowException() {
		contactDetailValidator.validateContactDetailCreationArgument(contactDetailMock);
		verify(contactDetailMock).validateForCreation();
	}

	@Test
	public void validateContactDetailCreationArgument_contactDetailIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailCreationArgument(null));
	}

	@Test
	public void validateContactDetailUpdateArgument_argsAreValid_doesNotThrowException() {
		when(contactDetailMock.getId()).thenReturn(contactDetailId);
		when(existingContactDetailMock.getUserId()).thenReturn(userId);
		when(contactDetailMock.getUserId()).thenReturn(userId);
		contactDetailValidator.validateContactDetailUpdateArgument(existingContactDetailMock, contactDetailMock, contactDetailId);
		verify(contactDetailMock).validateForUpdate();
	}

	@Test
	public void validateContactDetailUpdateArgument_contactDetailIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailUpdateArgument(existingContactDetailMock, null, contactDetailId));
	}

	@Test
	public void validateContactDetailUpdateArgument_contactDetailIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailUpdateArgument(existingContactDetailMock, contactDetailMock, null));
	}

	@Test
	public void validateContactDetailUpdateArgument_contactDetailIdNotEqualToPathContactDetailId_throwsException() {
		when(contactDetailMock.getId()).thenReturn(2L);
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailUpdateArgument(existingContactDetailMock, contactDetailMock, contactDetailId));
	}

	@Test
	public void validateContactDetailUpdateArgument_existingContactDetailUserIdNotEqualToContactDetailUserId_throwsException() {
		when(contactDetailMock.getId()).thenReturn(contactDetailId);
		when(existingContactDetailMock.getUserId()).thenReturn(userId);
		when(contactDetailMock.getUserId()).thenReturn(2L);
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailUpdateArgument(existingContactDetailMock, contactDetailMock, contactDetailId));
	}

	@Test
	public void validateContactDetailsFetchRequest_validArgs_doesNotThrowException() {
		contactDetailValidator.validateContactDetailsFetchRequest(1L, 1, 1);
	}

	@Test
	public void validateContactDetailsFetchRequest_userIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailsFetchRequest(null, 1, 1));
	}

	@Test
	public void validateContactDetailsFetchRequest_pageNumberIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailsFetchRequest(1L, null, 1));
	}

	@Test
	public void validateContactDetailsFetchRequest_pageSizeIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailsFetchRequest(1L, 1, null));
	}

	@Test
	public void validateContactDetailsFetchRequest_pageSizeIsTooBig_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailsFetchRequest(1L, 1, 11));
	}

	@Test
	public void validateContactDetailsCountFetchRequest_validArgs_doesNotThrowException() {
		contactDetailValidator.validateContactDetailsCountFetchRequest(1L);
	}

	@Test
	public void validateContactDetailsCountFetchRequest_userIdIsNull_throwsException() {
		assertThrows(InvalidDataException.class, () -> contactDetailValidator.validateContactDetailsCountFetchRequest(null));
	}
}
