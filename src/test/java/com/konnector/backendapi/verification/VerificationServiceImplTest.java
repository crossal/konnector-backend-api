package com.konnector.backendapi.verification;

import com.konnector.backendapi.verification.code.CodeGenerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceImplTest {

	@InjectMocks
	private VerificationService verificationService = new VerificationServiceImpl();

	@Mock
	private CodeGenerationService codeGenerationServiceMock;

	private long userId = 1;
	private String code = "code";

	@BeforeEach
	public void setup() {
		when(codeGenerationServiceMock.generateCode(anyInt())).thenReturn(code);
	}

	@Test
	public void createVerificationForUser_createsVerification() {
		Verification verification = verificationService.createEmailVerificationForUser(userId);

		verify(codeGenerationServiceMock, times(1)).generateCode(anyInt());
		assertEquals(userId, verification.getUserId());
		assertEquals(VerificationType.EMAIL, verification.getType());
		assertEquals(code, verification.getCode());
		assertNotNull(verification.getUrlToken());
		assertTrue(verification.getCodeAttemptsLeft() > 0);
		assertEquals(VerificationStatus.INCOMPLETE, verification.getStatus());
		assertNotNull(verification.getExpiresOn());
	}
}
