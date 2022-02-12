package com.konnector.backendapi.verification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.notification.EmailTransportWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/verification/verification-insert-data.sql" })
public class VerificationIT {

	private static final String USER_EMAIL_FOR_VERIFICATION = "email";
	private static final String USER_EMAIL_WITH_VERIFICATION_NOT_ALLOWED_TO_REVERIFY_YET = "email_2";
	private static final String USER_EMAIL_FOR_PASSWORD_RESET_WITH_TOKEN = "email_3";
	private static final String USER_EMAIL_WITH_PASSWORD_RESET_NOT_ALLOWED_AGAIN_YET = "email_4";
	private static final String USER_EMAIL_FOR_PASSWORD_RESET_WITH_CODE = "email_5";
	private static final String VERIFICATION_TOKEN = "token";
	private static final String VERIFICATION_CODE = "123456";
	private static final String PASSWORD = "password";

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;

	@MockBean
	private EmailTransportWrapper emailTransportWrapper;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final VerificationDTO verificationDTO = new VerificationDTO();

	@Test
	public void createEmailVerificationForUser_createsEmailVerification() {
		String url = "http://localhost:" + randomServerPort + "/api/verifications";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("usernameOrEmail", USER_EMAIL_FOR_VERIFICATION)
				.queryParam("type", VerificationType.EMAIL.getValue());
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void createEmailVerificationForUser_notAllowedReverifyYet_returnsError() {
		String url = "http://localhost:" + randomServerPort + "/api/verifications";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("usernameOrEmail", USER_EMAIL_WITH_VERIFICATION_NOT_ALLOWED_TO_REVERIFY_YET)
				.queryParam("type", VerificationType.EMAIL.getValue());
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), null, String.class);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void verifyUserEmail_byToken_verifiesUserEmail() {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("usernameOrEmail", USER_EMAIL_FOR_VERIFICATION)
				.queryParam("token", VERIFICATION_TOKEN)
				.queryParam("type", VerificationType.EMAIL.getValue());
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void verifyUserEmail_byCode_verifiesUserEmail() throws Exception {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("type", VerificationType.EMAIL.getValue());
		verificationDTO.setUsernameOrEmail(USER_EMAIL_FOR_VERIFICATION);
		verificationDTO.setCode(VERIFICATION_CODE);
		verificationDTO.setUserPassword(PASSWORD);
		String verificationJson = objectMapper.writeValueAsString(verificationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(verificationJson, headers);
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void verifyUserEmail_byIncorrectCodeThenCorrectCodeAfterExceededAttemptLimit_returnsError() throws JsonProcessingException {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("type", VerificationType.EMAIL.getValue());
		url = uriComponentsBuilder.build().toUriString();
		verificationDTO.setCode("some_wrong_code");
		verificationDTO.setUsernameOrEmail(USER_EMAIL_FOR_VERIFICATION);
		verificationDTO.setUserPassword(PASSWORD);
		String verificationJson = objectMapper.writeValueAsString(verificationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(verificationJson, headers);
		ResponseEntity<String> response = testRestTemplate.postForEntity(url, entity, String.class);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());

		verificationDTO.setCode(VERIFICATION_CODE);
		verificationJson = objectMapper.writeValueAsString(verificationDTO);
		entity = new HttpEntity<>(verificationJson, headers);
		response = testRestTemplate.postForEntity(url, entity, String.class);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void createPasswordResetForUser_createsPasswordReset() {
		String url = "http://localhost:" + randomServerPort + "/api/verifications";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("usernameOrEmail", USER_EMAIL_FOR_PASSWORD_RESET_WITH_TOKEN)
				.queryParam("type", VerificationType.PASSWORD.getValue());
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void createPasswordResetForUser_notAllowedResetPasswordAgainYet_returnsError() {
		String url = "http://localhost:" + randomServerPort + "/api/verifications";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("usernameOrEmail", USER_EMAIL_WITH_PASSWORD_RESET_NOT_ALLOWED_AGAIN_YET)
				.queryParam("type", VerificationType.PASSWORD.getValue());
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), null, String.class);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void resetPassword_withToken_resetsPassword() throws JsonProcessingException {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("passwordResetToken", VERIFICATION_TOKEN)
				.queryParam("type", VerificationType.PASSWORD.getValue());
		verificationDTO.setUsernameOrEmail(USER_EMAIL_FOR_PASSWORD_RESET_WITH_TOKEN);
		verificationDTO.setCode(VERIFICATION_CODE);
		verificationDTO.setUserPassword(PASSWORD);
		String verificationJson = objectMapper.writeValueAsString(verificationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(verificationJson, headers);
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void resetPassword_withCode_resetsPassword() throws JsonProcessingException {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("type", VerificationType.PASSWORD.getValue());
		verificationDTO.setUsernameOrEmail(USER_EMAIL_FOR_PASSWORD_RESET_WITH_CODE);
		verificationDTO.setCode(VERIFICATION_CODE);
		verificationDTO.setUserPassword(PASSWORD);
		String verificationJson = objectMapper.writeValueAsString(verificationDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(verificationJson, headers);
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
