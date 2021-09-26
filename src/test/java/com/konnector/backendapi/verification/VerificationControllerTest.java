package com.konnector.backendapi.verification;

import org.jeasy.random.EasyRandom;
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
	private final EasyRandom easyRandom = new EasyRandom();
	private final VerificationDTO verificationDTO = easyRandom.nextObject(VerificationDTO.class);

	@Test
	public void createEmailVerificationForUser_createsEmailVerification() {
		verificationController.createEmailVerificationForUser(verificationDTO.getUsernameOrEmail(), VerificationType.EMAIL.getValue());

		verify(verificationServiceMock).createEmailVerificationForUser(verificationDTO.getUsernameOrEmail());
	}

	@Test
	public void verifyUserEmailByCode_verifiesEmailByCode() {
		verificationController.verifyUserEmailByCode(verificationDTO, VerificationType.EMAIL.getValue());

		verify(verificationServiceMock).verifyEmailByCode(verificationDTO.getUsernameOrEmail(), verificationDTO.getCode());
	}

	@Test
	public void verifyUserEmailByToken_verifiesEmailByToken() {
		verificationController.verifyUserEmailByToken(usernameOrEmail, token, VerificationType.EMAIL.getValue());

		verify(verificationServiceMock).verifyEmailByUrlToken(usernameOrEmail, token);
	}

	@Test
	public void createPasswordResetForUser_createsPasswordReset() {
		verificationController.createPasswordResetForUser(usernameOrEmail, VerificationType.PASSWORD.getValue());

		verify(verificationServiceMock).createPasswordResetForUser(usernameOrEmail);
	}

	@Test
	public void resetUserPasswordWithToken_resetsUserPasswordWithToken() {
		verificationController.resetUserPasswordWithToken(verificationDTO, token, VerificationType.PASSWORD.getValue());

		verify(verificationServiceMock).resetPasswordWithToken(verificationDTO.getUsernameOrEmail(), verificationDTO.getUserPassword(), token);
	}

	@Test
	public void resetUserPasswordWithCode_resetsUserPasswordWithCode() {
		verificationController.resetUserPasswordWithCode(verificationDTO, VerificationType.PASSWORD.getValue());

		verify(verificationServiceMock).resetPasswordWithCode(verificationDTO.getUsernameOrEmail(), verificationDTO.getUserPassword(), verificationDTO.getCode());
	}
}
