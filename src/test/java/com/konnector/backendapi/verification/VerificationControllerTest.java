package com.konnector.backendapi.verification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VerificationControllerTest {

	@InjectMocks
	private VerificationController verificationController = new VerificationController();

	@Mock
	private VerificationService verificationServiceMock;

	private final String usernameOrEmail = "username_or_email";
	private final String token = "token";
	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final VerificationDTO verificationDTO = podamFactory.manufacturePojo(VerificationDTO.class);

	@Test
	public void verifyUserEmailByCode_verifiesEmailByCode() {
		verificationController.verifyUserEmailByCode(verificationDTO);

		verify(verificationServiceMock).verifyEmailByCode(verificationDTO.getUsernameOrEmail(), verificationDTO.getCode());
	}

	@Test
	public void verifyUserEmailByToken_verifiesEmailByToken() {
		verificationController.verifyUserEmailByToken(usernameOrEmail, token);

		verify(verificationServiceMock).verifyEmailByUrlToken(usernameOrEmail, token);
	}
}
