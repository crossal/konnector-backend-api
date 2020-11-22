package com.konnector.backendapi.verification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VerificationControllerTest {

	@InjectMocks
	private VerificationController verificationController = new VerificationController();

	@Mock
	private VerificationService verificationServiceMock;

	private final String usernameOrEmail = "username_or_email";
	private final String token = "token";
	private final String code = "code";

	@Test
	public void verifyUserEmailByCode_verifiesEmailByCode() {
		verificationController.verifyUserEmailByCode(usernameOrEmail, code);

		verify(verificationServiceMock).verifyEmailByCode(usernameOrEmail, code);
	}

	@Test
	public void verifyUserEmailByToken_verifiesEmailByToken() {
		verificationController.verifyUserEmailByToken(usernameOrEmail, token);

		verify(verificationServiceMock).verifyEmailByUrlToken(usernameOrEmail, token);
	}
}
