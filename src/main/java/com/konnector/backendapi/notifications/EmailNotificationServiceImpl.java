package com.konnector.backendapi.notifications;

import org.springframework.stereotype.Service;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

	@Override
	public void sendVerificationEmail(String email, String code, String urlToken) {

	}
}
