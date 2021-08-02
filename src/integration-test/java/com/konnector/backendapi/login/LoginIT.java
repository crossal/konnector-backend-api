package com.konnector.backendapi.login;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.user.UserDTO;
import org.jeasy.random.EasyRandom;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/login/login-insert-data.sql" })
public class LoginIT {

	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String HEADER_SET_COOKIE = "Set-Cookie";
	private static final String HEADER_SET_COOKIE_EXPECTED_ATTRIBUTES = "Secure; HttpOnly; SameSite=Strict";

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;
	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final AuthenticationDTO authenticationDTO = easyRandom.nextObject(AuthenticationDTO.class);

	@Test
	public void loginEndpoint_logsInUser() throws Exception {
		authenticationDTO.setUsernameOrEmail(EMAIL);
		authenticationDTO.setPassword(PASSWORD);
		String authenticationJson = objectMapper.writeValueAsString(authenticationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(authenticationJson, headers);

		ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + randomServerPort + "/api/authenticate", entity, String.class);

		assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
		assertNotNull(response.getBody());

		UserDTO loggedInUser = objectMapper.readValue(response.getBody(), UserDTO.class);
		assertEquals(EMAIL, loggedInUser.getEmail());

		assertFalse(response.getHeaders().isEmpty());
		assertTrue(response.getHeaders().containsKey(HEADER_SET_COOKIE));
		List<String> setCookieHeader = response.getHeaders().get(HEADER_SET_COOKIE);
		assertTrue(setCookieHeader.get(0).contains(HEADER_SET_COOKIE_EXPECTED_ATTRIBUTES));
	}

	@Test
	public void loginEndpoint_incorrectPassword_doesNotLogInUser() throws Exception {
		authenticationDTO.setUsernameOrEmail(EMAIL);
		authenticationDTO.setPassword("wrong_password");
		String authenticationJson = objectMapper.writeValueAsString(authenticationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(authenticationJson, headers);

		ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + randomServerPort + "/api/authenticate", entity, String.class);

		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
	}
}
