package com.konnector.backendapi.verification;

import com.konnector.backendapi.user.User;

public interface VerificationService {
	Verification createEmailVerificationForUser(String usernameOrEmail);
	Verification createEmailVerificationForUser(User user);
	void verifyEmailByUrlToken(String usernameOrEmail, String urlToken);
	void verifyEmailByCode(String usernameOrEmail, String code);
}
