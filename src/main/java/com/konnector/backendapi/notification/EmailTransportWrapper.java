package com.konnector.backendapi.notification;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import org.springframework.stereotype.Service;

@Service
public class EmailTransportWrapper {

	public void send(Message message) throws MessagingException {
		Transport.send(message);
	}
}
