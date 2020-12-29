package com.konnector.backendapi.notifications;

import com.konnector.backendapi.exceptions.EmailVerificationSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailNotificationServiceImplTest {

	private static final String RECIPIENT = "recipient@something.com";
	private static final String SENDER = "support@konnector.io";
	private static final String CODE = "code";
	private static final String URL_TOKEN = "token";
	private static final String SUBJECT = "Verify your email address";
	private static final String BODY = "Verification code: " + CODE + "\nVerification link: konnector.io/api/verifications/verify?token=" + URL_TOKEN;

	@InjectMocks
	private EmailNotificationServiceImpl emailNotificationService = new EmailNotificationServiceImpl();

	@Mock
	private EmailTransportWrapper emailTransportWrapperMock;

	@Captor
	private ArgumentCaptor<Message> messageCaptor;

	@BeforeEach
	public void setup() {
		emailNotificationService.init();
	}

	@Test
	public void sendVerificationEmail_exception_throwsCorrectException() throws MessagingException {
		doThrow(new MessagingException()).when(emailTransportWrapperMock).send(any());

		assertThrows(EmailVerificationSendException.class, () -> emailNotificationService.sendVerificationEmail(RECIPIENT, CODE, URL_TOKEN));
	}

	@Test
	public void sendVerificationEmail_sendsEmail() throws MessagingException, IOException {
		emailNotificationService.sendVerificationEmail(RECIPIENT, CODE, URL_TOKEN);

		verify(emailTransportWrapperMock, times(1)).send(messageCaptor.capture());
		Message message = messageCaptor.getValue();
		List<String> recipients = Arrays.asList(message.getAllRecipients()).stream().map(address -> ((InternetAddress) address).getAddress()).collect(Collectors.toList());
		assertTrue(recipients.contains(RECIPIENT));
		List<String> senders = Arrays.asList(message.getFrom()).stream().map(address -> ((InternetAddress) address).getAddress()).collect(Collectors.toList());
		assertTrue(senders.contains(SENDER));
		assertEquals(SUBJECT, message.getSubject());
		assertEquals(BODY, message.getContent().toString());
	}
}
