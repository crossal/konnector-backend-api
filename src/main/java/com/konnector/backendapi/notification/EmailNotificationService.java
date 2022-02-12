package com.konnector.backendapi.notification;

public interface EmailNotificationService {
	void sendVerificationEmail(String recipient, String code, String urlToken);
	void sendPasswordResetEmail(String recipient, String code, String urlToken);
}
