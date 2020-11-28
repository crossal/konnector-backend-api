package com.konnector.backendapi.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql("/data/truncate-all-data.sql")
public class UserIT {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;
	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final UserDTO userDTO = podamFactory.manufacturePojo(UserDTO.class);

	@Test
	public void createUserEndpoint_createsUser() throws Exception {
		userDTO.setEmail("someemail@userit.com");
		userDTO.setId(null);
		String userJson = objectMapper.writeValueAsString(userDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(userJson, headers);

		ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + randomServerPort + "/api/users", entity, String.class);

		assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());

		UserDTO createdUser = objectMapper.readValue(response.getBody(), UserDTO.class);
		assertNotNull(createdUser.getId());
		assertNull(createdUser.getPassword());
		assertEquals(userDTO.getEmail(), createdUser.getEmail());
		assertEquals(userDTO.getFirstName(), createdUser.getFirstName());
		assertEquals(userDTO.getLastName(), createdUser.getLastName());
		assertFalse(createdUser.isEmailVerified());
	}
}
