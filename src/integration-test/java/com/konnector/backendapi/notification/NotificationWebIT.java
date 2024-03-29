package com.konnector.backendapi.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konnector.backendapi.http.Headers;
import com.konnector.backendapi.session.SecurityConfig;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(SecurityConfig.class)
@TestPropertySource("classpath:application-integration-test.properties")
public class NotificationWebIT {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private NotificationService notificationServiceMock;
	@MockBean
	private ModelMapper modelMapperMock;
	@MockBean
	private UserDetailsService userDetailsService;
	@Mock
	private Notification notificationMock;

	private final ObjectMapper objectMapper = new ObjectMapper()
			.configure(MapperFeature.USE_ANNOTATIONS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	private final EasyRandom easyRandom = new EasyRandom();
	private final NotificationDTO notificationDTO = easyRandom.nextObject(NotificationDTO.class);

	@BeforeEach
	public void setup() throws JsonProcessingException {
		notificationDTO.getRecipient().setEmail(null);
		notificationDTO.getRecipient().setPassword(null);
		notificationDTO.getRecipient().setOldPassword(null);
		notificationDTO.getRecipient().setEmailVerified(null);
		notificationDTO.getSender().setPassword(null);
		notificationDTO.getSender().setOldPassword(null);
		notificationDTO.getSender().setEmailVerified(null);
		notificationDTO.getSender().setEmail(null);
	}

	@Test
	@WithMockUser
	public void getNotifications_returnsSuccessAndNotifications() throws Exception {
		List<Notification> notifications = List.of(notificationMock);
		List<NotificationDTO> notificationDTOs = List.of(notificationDTO);
		when(notificationServiceMock.getNotifications(1L, 1, 1)).thenReturn(notifications);
		when(notificationServiceMock.getNotificationsCount(1L)).thenReturn(10L);
		Type listType = new TypeToken<List<NotificationDTO>>() {}.getType();
		when(modelMapperMock.map(notifications, listType)).thenReturn(notificationDTOs);

		MvcResult result = mockMvc.perform(get("/api/notifications?user-id=1&page-number=1&page-size=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		List<NotificationDTO> notificationDTOsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

		assertEquals(notificationDTOs, notificationDTOsResponse);
		assertEquals("10", result.getResponse().getHeader(Headers.HEADER_TOTAL_COUNT));
	}

	@Test
	public void getNotifications_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(get("/api/notifications?user-id=1&page-number=1&page-size=1")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void deleteConnection_returnsSuccess() throws Exception {
		mockMvc.perform(delete("/api/notifications/1")).andExpect(status().isOk());
	}

	@Test
	public void deleteConnection_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(delete("/api/notifications/1")).andExpect(status().isUnauthorized());
	}
}
