package com.konnector.backendapi.contactdetail;

import jakarta.servlet.http.HttpServletResponse;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactDetailControllerTest {

	@InjectMocks
	private ContactDetailController contactDetailController = new ContactDetailController();

	@Mock
	private ContactDetailService contactDetailServiceMock;
	@Mock
	private ModelMapper modelMapperMock;
	@Mock
	private ContactDetail contactDetailMock;
	@Mock
	private HttpServletResponse httpServletResponseMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final ContactDetailDTO contactDetailDTO = easyRandom.nextObject(ContactDetailDTO.class);

	@Test
	public void createContactDetail_returnsSuccessAndCreatedContactDetail() {
		when(modelMapperMock.map(any(ContactDetailDTO.class), eq(ContactDetail.class))).thenReturn(contactDetailMock);
		when(contactDetailServiceMock.createContactDetail(contactDetailMock)).thenReturn(contactDetailMock);
		when(modelMapperMock.map(contactDetailMock, ContactDetailDTO.class)).thenReturn(contactDetailDTO);

		ContactDetailDTO result = contactDetailController.createContactDetail(contactDetailDTO);

		assertEquals(contactDetailDTO, result);
	}

	@Test
	public void updateContactDetail_returnsSuccessAndUpdatedContactDetail() {
		when(modelMapperMock.map(any(ContactDetailDTO.class), eq(ContactDetail.class))).thenReturn(contactDetailMock);
		when(contactDetailServiceMock.updateContactDetail(contactDetailMock, 1L)).thenReturn(contactDetailMock);
		when(modelMapperMock.map(contactDetailMock, ContactDetailDTO.class)).thenReturn(contactDetailDTO);

		ContactDetailDTO result = contactDetailController.updateContactDetail(contactDetailDTO, 1L);

		assertEquals(contactDetailDTO, result);
	}

	@Test
	public void getContactDetails_returnsSuccessAndContactDetails() {
		List<ContactDetail> contactDetails = List.of(contactDetailMock);
		List<ContactDetailDTO> contactDetailDTOs = List.of(contactDetailDTO);
		when(contactDetailServiceMock.getContactDetails(1L, 1, 1)).thenReturn(contactDetails);
		Type listType = new TypeToken<List<ContactDetailDTO>>() {}.getType();
		when(modelMapperMock.map(contactDetails, listType)).thenReturn(contactDetailDTOs);

		List<ContactDetailDTO> result = contactDetailController.getContactDetails(1L, 1, 1, httpServletResponseMock);

		assertEquals(contactDetailDTOs, result);
	}

	@Test
	public void deleteContactDetail_returnsSuccessAndDeletesContactDetail() {
		contactDetailController.deleteContactDetail(1L);

		verify(contactDetailServiceMock).deleteContactDetail(1L);
	}
}
