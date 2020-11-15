package com.konnector.backendapi.verification;

import com.konnector.backendapi.verification.code.CodeGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements VerificationService {

	private static final int CODE_LENGTH = 4;
	private static final int CODE_ATTEMPTS = 5;
	private static final int URL_TOKEN_EXPIRATION_IN_DAYS = 5;

	@Autowired
	private CodeGenerationService codeGenerationService;

	@Override
	public Verification createEmailVerificationForUser(long userId) {
		Verification verification = new Verification(userId, VerificationType.EMAIL, codeGenerationService.generateCode(CODE_LENGTH), CODE_ATTEMPTS, UUID.randomUUID().toString(), LocalDateTime.now().plusDays(URL_TOKEN_EXPIRATION_IN_DAYS));
		return verification;
	}
}
