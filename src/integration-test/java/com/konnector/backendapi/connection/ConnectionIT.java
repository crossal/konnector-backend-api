package com.konnector.backendapi.connection;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.AuthenticatedTest;
import com.konnector.backendapi.session.SecurityTestConfig;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SecurityTestConfig.class)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/connection/connection-insert-data.sql" })
public class ConnectionIT extends AuthenticatedTest {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;
	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final ConnectionDTO connectionDTO = easyRandom.nextObject(ConnectionDTO.class);

	@Test
	public void createConnectionEndpoint_createsContactDetail() throws Exception {
		connectionDTO.setId(null);
		connectionDTO.setRequesterId(1L);
		connectionDTO.setRequesteeId(3L);
		connectionDTO.setStatus(ConnectionStatus.REQUESTED);
		String connectionJson = objectMapper.writeValueAsString(connectionDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = getEntityWithAuth(connectionJson, headers);

		ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + randomServerPort + "/api/connections", entity, String.class);

		assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());

		ConnectionDTO createdConnectionDTO = objectMapper.readValue(response.getBody(), ConnectionDTO.class);
		assertNotNull(createdConnectionDTO.getId());
		assertEquals(connectionDTO.getRequesteeId(), createdConnectionDTO.getRequesteeId());
		assertEquals(connectionDTO.getRequesterId(), createdConnectionDTO.getRequesterId());
		assertEquals(connectionDTO.getStatus(), createdConnectionDTO.getStatus());
	}

	@Test
	public void updateConnectionEndpoint_updatesConnection() throws Exception {
		connectionDTO.setId(1L);
		connectionDTO.setRequesteeId(1L);
		connectionDTO.setRequesterId(2L);
		connectionDTO.setStatus(ConnectionStatus.ACCEPTED);
		String connectionJson = objectMapper.writeValueAsString(connectionDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = getEntityWithAuth(connectionJson, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/connections/1", HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());

		ConnectionDTO updatedConnectionDTO = objectMapper.readValue(response.getBody(), ConnectionDTO.class);
		assertEquals(connectionDTO, updatedConnectionDTO);
	}

	@Test
	public void deleteConnectionEndpoint_deletesConnection() {
		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/connections/1", HttpMethod.DELETE, getEntityWithAuth(null, null), String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
	}
}
