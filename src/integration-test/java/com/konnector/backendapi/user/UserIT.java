package com.konnector.backendapi.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.notifications.EmailTransportWrapper;
import com.konnector.backendapi.session.SecurityConfigTest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SecurityConfigTest.class)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/user/user-insert-data.sql" })
//@EnableAutoConfiguration(exclude = {
//		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
//		org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class/*,
//		org.springframework.boot.autoconfigure.security.servlet.FallbackWebSecurityAutoConfiguration.class,
//		org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration.class*/
//})
public class UserIT {

	private TestRestTemplate testRestTemplate = new TestRestTemplate("user", "password");
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
	@WithMockUser(username = "username", password = "password", roles = "USER")
//	@WithUserDetails("username")
	public void getUserEndpoint_getsUser() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

//		ResponseEntity<String> response = testRestTemplate.withBasicAuth("spring", "secret").getForEntity("http://localhost:" + randomServerPort + "/api/users/1", String.class);
//		ResponseEntity<String> response = testRestTemplate.with(user("admin").roles("USER","ADMIN")).getForEntity("http://localhost:" + randomServerPort + "/api/users/1", String.class);
		ResponseEntity<String> response = testRestTemplate.getForEntity("http://localhost:" + randomServerPort + "/api/users/1", String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());

		UserDTO createdUser = objectMapper.readValue(response.getBody(), UserDTO.class);
		assertEquals(1, createdUser.getId());
	}
}
