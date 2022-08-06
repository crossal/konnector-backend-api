package com.konnector.backendapi.notification;

import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

@Service
public class EmailTransportWrapper {

	public void send(Message message) throws MessagingException {
		Transport.send(message);
	}
}
