package com.konnector.backendapi.notification;

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
	private static final String SENDER_NAME = "Konnector";
	private static final String CODE = "code";
	private static final String URL_TOKEN = "token";
	private static final String VERIFICATION_SUBJECT = "Verify your email address";
	private static final String VERIFICATION_BODY = "Verification code: " + CODE + "\nVerification link: konnector.io/verifications/verify?type=0&token=" + URL_TOKEN + "&username-or-email=" + RECIPIENT;
	private static final String PASSWORD_RESET_SUBJECT = "Reset your password";
	private static final String PASSWORD_RESET_BODY = "Password Reset code: " + CODE + "\nPassword Reset link: konnector.io/verifications/verify?type=1&token=" + URL_TOKEN;

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
		List<String> recipients = Arrays.stream(message.getAllRecipients()).map(address -> ((InternetAddress) address).getAddress()).collect(Collectors.toList());
		assertTrue(recipients.contains(RECIPIENT));
		List<String> senders = Arrays.stream(message.getFrom()).map(address -> ((InternetAddress) address).getAddress()).collect(Collectors.toList());
		assertTrue(senders.contains(SENDER));
		List<String> senderNames = Arrays.stream(message.getFrom()).map(address -> ((InternetAddress) address).getPersonal()).collect(Collectors.toList());
		assertTrue(senderNames.contains(SENDER_NAME));
		assertEquals(VERIFICATION_SUBJECT, message.getSubject());
		assertEquals(VERIFICATION_BODY, message.getContent().toString());
	}

	@Test
	public void sendPasswordResetEmail_exception_throwsCorrectException() throws MessagingException {
		doThrow(new MessagingException()).when(emailTransportWrapperMock).send(any());

		assertThrows(EmailVerificationSendException.class, () -> emailNotificationService.sendPasswordResetEmail(RECIPIENT, CODE, URL_TOKEN));
	}

	@Test
	public void sendPasswordResetEmail_sendsEmail() throws MessagingException, IOException {
		emailNotificationService.sendPasswordResetEmail(RECIPIENT, CODE, URL_TOKEN);

		verify(emailTransportWrapperMock, times(1)).send(messageCaptor.capture());
		Message message = messageCaptor.getValue();
		List<String> recipients = Arrays.stream(message.getAllRecipients()).map(address -> ((InternetAddress) address).getAddress()).collect(Collectors.toList());
		assertTrue(recipients.contains(RECIPIENT));
		List<String> senders = Arrays.stream(message.getFrom()).map(address -> ((InternetAddress) address).getAddress()).collect(Collectors.toList());
		assertTrue(senders.contains(SENDER));
		List<String> senderNames = Arrays.stream(message.getFrom()).map(address -> ((InternetAddress) address).getPersonal()).collect(Collectors.toList());
		assertTrue(senderNames.contains(SENDER_NAME));
		assertEquals(PASSWORD_RESET_SUBJECT, message.getSubject());
		assertEquals(PASSWORD_RESET_BODY, message.getContent().toString());
	}
}
