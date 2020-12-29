package com.konnector.backendapi.verification;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/verification/verification-insert-data.sql" })
public class VerificationIT {

	private static final String USER_EMAIL = "verification_test_email";
	private static final String VERIFICATION_TOKEN = "verification_test_token";
	private static final String VERIFICATION_CODE = "123456";

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final VerificationDTO verificationDTO = new VerificationDTO();

	@BeforeEach
	public void setup() {
		verificationDTO.setUsernameOrEmail(USER_EMAIL);
		verificationDTO.setCode(VERIFICATION_CODE);
	}

	@Test
	public void verifyUserEmail_byToken_verifiesUserEmail() {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("usernameOrEmail", USER_EMAIL)
				.queryParam("token", VERIFICATION_TOKEN);
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void verifyUserEmail_byCode_verifiesUserEmail() throws Exception {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		String verificationJson = objectMapper.writeValueAsString(verificationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(verificationJson, headers);
		ResponseEntity<String> response = testRestTemplate.postForEntity(url, entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void verifyUserEmail_byIncorrectCodeThenCorrectCodeAfterExceededAttemptLimit_returnsErrors() throws Exception {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		verificationDTO.setCode("some_wrong_code");
		String verificationJson = objectMapper.writeValueAsString(verificationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(verificationJson, headers);
		ResponseEntity<String> response = testRestTemplate.postForEntity(url, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		verificationDTO.setCode(VERIFICATION_CODE);
		verificationJson = objectMapper.writeValueAsString(verificationDTO);
		entity = new HttpEntity<>(verificationJson, headers);
		response = testRestTemplate.postForEntity(url, entity, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
