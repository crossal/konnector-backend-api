package com.konnector.backendapi.verification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-integration-test.properties")
@Sql({ "/data/truncate-all-data.sql", "/data/verification/verification-insert-data.sql" })
public class VerificationIT {

	private static final String USER_EMAIL = "verification_test_email";
	private static final String VERIFICATION_TOKEN = "verification_test_token";

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	@LocalServerPort
	int randomServerPort;

	@Test
	public void verifyUserEmail_verifiesUserEmail() {
		String url = "http://localhost:" + randomServerPort + "/api/verifications/verify";
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url)
				.queryParam("usernameOrEmail", USER_EMAIL)
				.queryParam("token", VERIFICATION_TOKEN);
		ResponseEntity<String> response = testRestTemplate.postForEntity(uriComponentsBuilder.build().toUriString(), null, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
