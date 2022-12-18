package com.konnector.backendapi.notification;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.konnector.backendapi.AuthenticatedTest;
import com.konnector.backendapi.session.SecurityTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SecurityTestConfig.class)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/notification/notification-insert-data.sql" })
public class NotificationIT extends AuthenticatedTest {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;
	private final ObjectMapper objectMapper = new ObjectMapper()
			.configure(MapperFeature.USE_ANNOTATIONS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;

	@Test
	public void getNotificationsEndpoint_getsNotifications() throws Exception {
		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/notifications?user-id=1&page-number=2&page-size=2", HttpMethod.GET, getEntityWithAuth(null, null), String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
		assertNotNull(response.getBody());

		List<NotificationDTO>  notificationDTOs = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
		assertEquals(2, notificationDTOs.size());
		assertTrue(notificationDTOs.get(0).getCreatedOn().compareTo(notificationDTOs.get(1).getCreatedOn()) >= 0);
	}

	@Test
	public void deleteNotificationEndpoint_deletesNotification() {
		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/notifications/1", HttpMethod.DELETE, getEntityWithAuth(null, null), String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
	}
}
