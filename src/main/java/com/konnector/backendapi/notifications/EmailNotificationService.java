package com.konnector.backendapi.notifications;

public interface EmailNotificationService {
	void sendVerificationEmail(String email, String code, String urlToken);
}
