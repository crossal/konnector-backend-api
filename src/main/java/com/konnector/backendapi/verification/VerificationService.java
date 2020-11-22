package com.konnector.backendapi.verification;

public interface VerificationService {
	Verification createEmailVerificationForUser(long userId);
	void verifyEmailByUrlToken(String usernameOrEmail, String urlToken);
	void verifyEmailByCode(String usernameOrEmail, String code);
}
