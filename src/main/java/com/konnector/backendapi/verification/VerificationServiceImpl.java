package com.konnector.backendapi.verification;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationServiceImpl implements VerificationService {

	private static final int CODE_LENGTH = 4;
	private static final int CODE_ATTEMPTS = 5;
	private static final int URL_TOKEN_EXPIRATION_IN_DAYS = 5;

	@Autowired
	private CodeGenerationService codeGenerationService;

	@Override
	public Verification createVerificationForUser(long userId) {
		Verification verification = new Verification(userId, codeGenerationService.generateCode(CODE_LENGTH), CODE_ATTEMPTS, UUID.randomUUID().toString(), LocalDateTime.now().plusDays(URL_TOKEN_EXPIRATION_IN_DAYS));
		return verification;
	}
}
