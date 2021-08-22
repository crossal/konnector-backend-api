package com.konnector.backendapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.login.AuthenticationDTO;
import com.konnector.backendapi.session.SecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SecurityTestConfig.class)
public abstract class AuthenticatedTest {

	private static final String JSESSIONID_COOKIE_NAME = "JSESSIONID";
	private static final TestRestTemplate testRestTemplate = new TestRestTemplate();
	private static final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@LocalServerPort
	int randomServerPort;

	protected String jSessionIdCookie;

	protected HttpHeaders getHttpHeadersWithAuth(HttpHeaders httpHeadersIn) {
		HttpHeaders httpHeaders = new HttpHeaders();

		if (httpHeadersIn != null) {
			httpHeaders.addAll(httpHeadersIn);
		}

		httpHeaders.add(HttpHeaders.COOKIE, jSessionIdCookie);

		return httpHeaders;
	}

	protected HttpEntity getEntityWithAuth(String body, HttpHeaders httpHeaders) {
		return new HttpEntity(body, getHttpHeadersWithAuth(httpHeaders));
	}

	@BeforeEach
	protected void setup() throws Exception {
		AuthenticationDTO authenticationDTO = new AuthenticationDTO();
		authenticationDTO.setUsernameOrEmail(SecurityTestConfig.USERNAME);
		authenticationDTO.setPassword(SecurityTestConfig.PASSWORD);
		String authenticationJson = objectMapper.writeValueAsString(authenticationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(authenticationJson, headers);

		ResponseEntity<String> response = testRestTemplate.postForEntity("http://localhost:" + randomServerPort + "/api/authenticate", entity, String.class);

		HttpHeaders responseHeaders = response.getHeaders();
		List<String> setCookiesCookies = responseHeaders.get(HttpHeaders.SET_COOKIE);
		String cookie = setCookiesCookies.get(0);
		String jSessionIdValue = extractCookieValue(cookie, JSESSIONID_COOKIE_NAME);
		jSessionIdCookie = constructCookie(JSESSIONID_COOKIE_NAME, jSessionIdValue);
	}

	private String extractCookieValue(String cookie, String cookieName) {
		return cookie.substring(cookie.indexOf(cookieName + "=") + (cookieName.length() + 1), cookie.indexOf(";"));
	}

	private String constructCookie(String cookieName, String cookieValue) {
		return cookieName + "=" + cookieValue + ";";
	}
}
