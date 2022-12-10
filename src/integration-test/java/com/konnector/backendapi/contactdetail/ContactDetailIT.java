package com.konnector.backendapi.contactdetail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.AuthenticatedTest;
import com.konnector.backendapi.http.Headers;
import com.konnector.backendapi.session.SecurityTestConfig;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SecurityTestConfig.class)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/contactdetail/contact-detail-insert-data.sql" })
public class ContactDetailIT extends AuthenticatedTest {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;
	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final ContactDetailDTO contactDetailDTO = easyRandom.nextObject(ContactDetailDTO.class);

	@Test
	public void createContactDetailEndpoint_createsContactDetail() throws Exception {
		contactDetailDTO.setId(null);
		contactDetailDTO.setUserId(1L);
		String contactDetailJson = objectMapper.writeValueAsString(contactDetailDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = getEntityWithAuth(contactDetailJson, headers);

		ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + randomServerPort + "/api/contact-details", entity, String.class);

		assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
		assertNotNull(response.getBody());

		ContactDetailDTO createdContactDetailDTO = objectMapper.readValue(response.getBody(), ContactDetailDTO.class);
		assertNotNull(createdContactDetailDTO.getId());
		assertEquals(contactDetailDTO.getUserId(), createdContactDetailDTO.getUserId());
		assertEquals(contactDetailDTO.getType(), createdContactDetailDTO.getType());
		assertEquals(contactDetailDTO.getValue(), createdContactDetailDTO.getValue());
	}

	@Test
	public void updateContactDetailEndpoint_updatesContactDetail() throws Exception {
		contactDetailDTO.setId(1L);
		contactDetailDTO.setUserId(1L);
		contactDetailDTO.setType("some new type");
		contactDetailDTO.setType("some new value");
		String contactDetailJson = objectMapper.writeValueAsString(contactDetailDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = getEntityWithAuth(contactDetailJson, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/contact-details/1", HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
		assertNotNull(response.getBody());

		ContactDetailDTO updatedContactDetailDTO = objectMapper.readValue(response.getBody(), ContactDetailDTO.class);
		assertEquals(contactDetailDTO.getId(), updatedContactDetailDTO.getId());
		assertEquals(contactDetailDTO.getType(), updatedContactDetailDTO.getType());
		assertEquals(contactDetailDTO.getValue(), updatedContactDetailDTO.getValue());
	}

	@Test
	public void getContactDetailsEndpoint_getsContactDetails() throws Exception {
		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/contact-details?user-id=1&page-number=2&page-size=2", HttpMethod.GET, getEntityWithAuth(null, null), String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
		assertNotNull(response.getBody());

		List<ContactDetailDTO> contactDetailDTOs = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
		assertEquals(2, contactDetailDTOs.size());
		assertEquals("c", contactDetailDTOs.get(0).getType());
		assertEquals("d", contactDetailDTOs.get(1).getType());
		assertEquals("6", response.getHeaders().get(Headers.HEADER_TOTAL_COUNT).get(0));
	}

	@Test
	public void deleteContactDetailEndpoint_deletesContactDetail() {
		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/contact-details/1", HttpMethod.DELETE, getEntityWithAuth(null, null), String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
	}
}
