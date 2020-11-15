package com.konnector.backendapi.verification;

public interface VerificationService {
	Verification createEmailVerificationForUser(long userId);
}
