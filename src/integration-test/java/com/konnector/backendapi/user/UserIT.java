package com.konnector.backendapi.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.AuthenticatedTest;
import com.konnector.backendapi.notification.EmailTransportWrapper;
import com.konnector.backendapi.session.SecurityTestConfig;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SecurityTestConfig.class)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/user/user-insert-data.sql" })
public class UserIT extends AuthenticatedTest {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;
	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final UserDTO userDTO = easyRandom.nextObject(UserDTO.class);

	@MockBean
	private EmailTransportWrapper emailTransportWrapper;

	@Test
	public void createUserEndpoint_createsUser() throws Exception {
		userDTO.setEmail("someemail@userit.com");
		userDTO.setId(null);
		userDTO.setEmailVerified(false);
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

	@Test
	public void updateUserEndpoint_updatesUser() throws Exception {
		userDTO.setEmail("test@email.com");
		userDTO.setUsername("username");
		userDTO.setFirstName("first_name_new");
		userDTO.setLastName("last_name_new");
		userDTO.setEmailVerified(true);
		userDTO.setPassword("new_password");
		userDTO.setOldPassword("password");
		userDTO.setId(1L);
		String userJson = objectMapper.writeValueAsString(userDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = getEntityWithAuth(userJson, headers);

		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/users/1", HttpMethod.PUT, entity, String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());

		UserDTO updatedUser = objectMapper.readValue(response.getBody(), UserDTO.class);
		assertEquals(userDTO.getId(), updatedUser.getId());
		assertNull(updatedUser.getPassword());
		assertEquals(userDTO.getEmail(), updatedUser.getEmail());
		assertEquals(userDTO.getFirstName(), updatedUser.getFirstName());
		assertEquals(userDTO.getLastName(), updatedUser.getLastName());
		assertEquals(userDTO.isEmailVerified(), updatedUser.isEmailVerified());
	}

	@Test
	public void getUserEndpoint_getsUser() throws Exception {
		ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:" + randomServerPort + "/api/users/1", HttpMethod.GET, getEntityWithAuth(null, null), String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());

		UserDTO user = objectMapper.readValue(response.getBody(), UserDTO.class);
		assertEquals(1, user.getId());
	}

	// TODO: getUsers
}
