package com.konnector.backendapi.contactdetail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.http.Headers;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactDetailController.class)
@TestPropertySource("classpath:application-integration-test.properties")
public class ContactDetailWebIT {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ContactDetailService contactDetailServiceMock;
	@MockBean
	private ModelMapper modelMapperMock;
	@MockBean
	private UserDetailsService userDetailsService;
	@Mock
	private ContactDetail contactDetailMock;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final ContactDetailDTO contactDetailDTO = easyRandom.nextObject(ContactDetailDTO.class);
	private String contactDetailJson;

	@BeforeEach
	public void setup() throws JsonProcessingException {
		contactDetailJson = objectMapper.writeValueAsString(contactDetailDTO);
	}

	@Test
	@WithMockUser
	public void createContactDetail_returnsSuccessAndCreatedContactDetail() throws Exception {
		when(modelMapperMock.map(any(ContactDetailDTO.class), eq(ContactDetail.class))).thenReturn(contactDetailMock);
		when(contactDetailServiceMock.createContactDetail(contactDetailMock)).thenReturn(contactDetailMock);
		when(modelMapperMock.map(contactDetailMock, ContactDetailDTO.class)).thenReturn(contactDetailDTO);

		MvcResult result = mockMvc.perform(post("/api/contact-details").contentType(MediaType.APPLICATION_JSON).content(contactDetailJson)).andExpect(status().isCreated()).andReturn();
		ContactDetailDTO contactDetailDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContactDetailDTO.class);

		assertEquals(contactDetailDTO, contactDetailDTOResponse);
	}

	@Test
	public void createContactDetail_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(post("/api/contact-details").contentType(MediaType.APPLICATION_JSON).content(contactDetailJson)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void updateContactDetail_returnsSuccessAndUpdatedContactDetail() throws Exception {
		when(modelMapperMock.map(any(ContactDetailDTO.class), eq(ContactDetail.class))).thenReturn(contactDetailMock);
		when(contactDetailServiceMock.updateContactDetail(contactDetailMock, 1L)).thenReturn(contactDetailMock);
		when(modelMapperMock.map(contactDetailMock, ContactDetailDTO.class)).thenReturn(contactDetailDTO);

		MvcResult result = mockMvc.perform(put("/api/contact-details/1").contentType(MediaType.APPLICATION_JSON).content(contactDetailJson)).andExpect(status().isOk()).andReturn();
		ContactDetailDTO contactDetailDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ContactDetailDTO.class);

		assertEquals(contactDetailDTO, contactDetailDTOResponse);
	}

	@Test
	public void updateContactDetail_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(put("/api/contact-details/1").contentType(MediaType.APPLICATION_JSON).content(contactDetailJson)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void getContactsDetails_returnsSuccessAndContactDetails() throws Exception {
		List<ContactDetail> contactDetails = List.of(contactDetailMock);
		List<ContactDetailDTO> contactDetailDTOs = List.of(contactDetailDTO);
		when(contactDetailServiceMock.getContactDetails(1L, 1, 1)).thenReturn(contactDetails);
		when(contactDetailServiceMock.getContactDetailsCount(1L)).thenReturn(10L);
		Type listType = new TypeToken<List<ContactDetailDTO>>() {}.getType();
		when(modelMapperMock.map(contactDetails, listType)).thenReturn(contactDetailDTOs);

		MvcResult result = mockMvc.perform(get("/api/contact-details?userId=1&pageNumber=1&pageSize=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		List<ContactDetailDTO> contactDetailDTOsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(contactDetailDTOs, contactDetailDTOsResponse);
		assertEquals("10", result.getResponse().getHeader(Headers.HEADER_TOTAL_COUNT));
	}

	@Test
	public void getContactDetails_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(get("/api/contact-details?userId=1&pageNumber=1&pageSize=1")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void deleteContactDetail_returnsSuccess() throws Exception {
		mockMvc.perform(delete("/api/contact-details/1")).andExpect(status().isOk());
	}

	@Test
	public void deleteContactDetail_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(delete("/api/contact-details/1")).andExpect(status().isUnauthorized());
	}
}
