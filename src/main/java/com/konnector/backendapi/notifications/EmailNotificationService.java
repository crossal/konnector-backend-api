package com.konnector.backendapi.notifications;

public interface EmailNotificationService {
	void sendVerificationEmail(String recipient, String code, String urlToken);
	void sendPasswordResetEmail(String recipient, String code, String urlToken);
}
