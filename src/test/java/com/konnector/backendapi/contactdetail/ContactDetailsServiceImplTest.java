package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserAuthorizationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactDetailsServiceImplTest {

	private ContactDetailService contactDetailService;

	@Mock
	private Dao<ContactDetail> contactDetailDaoMock;
	@Mock
	private Dao<User> userDaoMock;
	@Mock
	private ContactDetailValidator contactDetailValidatorMock;
	@Mock
	private AuthenticationFacade authenticationFacadeMock;
	@Mock
	private UserAuthorizationValidator userAuthorizationValidatorMock;
	@Mock
	private ContactDetailRepository contactDetailRepositoryMock;
	@Mock
	private Authentication authenticationMock;
	@Mock
	private ContactDetail contactDetailMock;
	@Mock
	private Page pageMock;
	@Mock
	private User userMock;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	@BeforeEach
	public void setup() {
		contactDetailService = new ContactDetailServiceImpl(contactDetailDaoMock, userDaoMock,
				contactDetailValidatorMock, userAuthorizationValidatorMock, authenticationFacadeMock,
				contactDetailRepositoryMock);
	}

	@Test
	public void createContactDetail_createsContactDetail() {
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		ContactDetail createdContactDetail = contactDetailService.createContactDetail(contactDetailMock);

		verify(contactDetailValidatorMock).validateContactDetailCreationArgument(contactDetailMock);
		verify(contactDetailDaoMock).save(contactDetailMock);
		assertEquals(contactDetailMock, createdContactDetail);
	}

	@Test
	public void updateContactDetail_contactDetailNotFound_throwsException() {
		Long contactDetailId = 1L;
		when(contactDetailDaoMock.get(contactDetailId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> contactDetailService.updateContactDetail(contactDetailMock, contactDetailId));
	}

	@Test
	public void updateUser_updatesUser() {
		Long contactDetailId = 1L;
		Long userId = 1L;
		when(contactDetailDaoMock.get(contactDetailId)).thenReturn(Optional.of(contactDetailMock));
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);
		when(contactDetailMock.getUserId()).thenReturn(userId);

		ContactDetail updatedContactDetail = contactDetailService.updateContactDetail(contactDetailMock, contactDetailId);

		assertEquals(contactDetailMock, updatedContactDetail);
		verify(contactDetailValidatorMock).validateContactDetailUpdateArgument(contactDetailMock, contactDetailMock, contactDetailId);
		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(contactDetailMock).merge(contactDetailMock);
		verify(contactDetailDaoMock).update(contactDetailMock);
	}

	@Test
	public void getContactDetails_getsContactDetails() {
		Long userId = 1L;
		Integer pageNumber = 2;
		Integer pageSize = 5;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		List<ContactDetail> contactDetails = List.of(contactDetailMock);
		when(contactDetailRepositoryMock.findByUserId(eq(userId), any(Pageable.class))).thenReturn(pageMock);
		when(pageMock.getContent()).thenReturn(contactDetails);

		List<ContactDetail> returnedContactDetails = contactDetailService.getContactDetails(userId, pageNumber, pageSize);

		assertEquals(contactDetails, returnedContactDetails);
		verify(contactDetailValidatorMock).validateContactDetailsFetchRequest(userId, pageNumber, pageSize);
		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(contactDetailRepositoryMock).findByUserId(eq(userId), pageableCaptor.capture());
		Pageable pageable = pageableCaptor.getValue();
		assertEquals(pageNumber - 1, pageable.getPageNumber());
		assertEquals(pageSize, pageable.getPageSize());
		Sort.Order order = pageable.getSort().stream().iterator().next();
		assertEquals("type", order.getProperty());
		assertEquals(Sort.Direction.ASC, order.getDirection());
	}

	@Test
	public void getContactDetailsCount_getsContactDetailsCount() {
		long count = 10L;
		Long userId = 1L;
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		when(contactDetailRepositoryMock.countByUserId(userId)).thenReturn(count);

		long returnedContactDetailsCount = contactDetailService.getContactDetailsCount(userId);

		assertEquals(count, returnedContactDetailsCount);
		verify(contactDetailValidatorMock).validateContactDetailsCountFetchRequest(userId);
		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(contactDetailRepositoryMock).countByUserId(userId);
	}

	@Test
	public void deleteContactDetail_deletesContactDetail() {
		Long contactDetailId = 1L;
		Long userId = 1L;
		when(contactDetailDaoMock.get(contactDetailId)).thenReturn(Optional.of(contactDetailMock));
		when(contactDetailMock.getUserId()).thenReturn(userId);
		when(userMock.getId()).thenReturn(userId);
		when(userDaoMock.get(userId)).thenReturn(Optional.of(userMock));
		when(authenticationFacadeMock.getAuthentication()).thenReturn(authenticationMock);

		contactDetailService.deleteContactDetail(contactDetailId);

		verify(userAuthorizationValidatorMock).validateUserRequest(userId, authenticationMock);
		verify(contactDetailDaoMock).delete(contactDetailMock);
	}

	@Test
	public void deleteContactDetail_contactDetailNotFound_throwsException() {
		Long contactDetailId = 1L;
		when(contactDetailDaoMock.get(contactDetailId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> contactDetailService.deleteContactDetail(contactDetailId));
	}
}
