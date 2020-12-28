package com.konnector.backendapi.notifications;

import com.konnector.backendapi.exceptions.EmailVerificationSendException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Properties;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

	private static final Logger LOGGER = LogManager.getLogger(EmailNotificationServiceImpl.class);

	private static final String FROM_ADDRESS = "support@konnector.io";
	private static final String PROPERTY_KEY_HOST = "mail.smtp.host";
	private static final String HOST = "smtp.gmail.com";//"localhost";
	private static final String PROPERTY_KEY_PORT = "mail.smtp.port";
	private static final String PORT = "587";
	private static final String PROPERTY_KEY_STARTTLS = "mail.smtp.starttls.enable";
	private static final String STARTTLS = "true";
	private static final String PROPERTY_KEY_AUTH = "mail.smtp.auth";
	private static final String AUTH = "true";
	private static final String VERIFICATION_EMAIL_SUBJECT = "Verify your email address";
	private static final String VERIFICATION_URL_TEMPLATE = "konnector.io/api/verifications/verify/{0}";
	private static final String VERIFICATION_EMAIL_BODY_TEMPLATE = "Verification code: {0}\nVerification link: {1}";

	@Value("${email.username}")
	private String emailUserName;
	@Value("${email.password}")
	private String emailPassword;

	@Override
	public void sendVerificationEmail(String email, String code, String urlToken) {
		Properties properties = System.getProperties();
		properties.setProperty(PROPERTY_KEY_HOST, HOST);
		properties.setProperty(PROPERTY_KEY_PORT, PORT);
		properties.setProperty(PROPERTY_KEY_AUTH, AUTH);
		properties.setProperty(PROPERTY_KEY_STARTTLS, STARTTLS);
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUserName, emailPassword);
			}
		});

		MimeMessage message = new MimeMessage(session);

		String verificationUrl = getVerificationUrl(urlToken);
		String verificationBody = getVerificationBody(code, verificationUrl);

		try {
			message.setFrom(new InternetAddress(FROM_ADDRESS));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject(VERIFICATION_EMAIL_SUBJECT);
			message.setText(verificationBody);

			Transport.send(message);
		} catch (Exception e) {
			LOGGER.error("Verification email could not be sent with exception: {}", e);
			throw new EmailVerificationSendException("Verification email could not be sent");
		}
	}

	private String getVerificationUrl(String urlToken) {
		Object[] params = new Object[]{urlToken};
		String verificationUrl = MessageFormat.format(VERIFICATION_URL_TEMPLATE, params);

		return  verificationUrl;
	}

	private String getVerificationBody(String code, String verificationUrl) {
		Object[] params = new Object[]{code, verificationUrl};
		String verificationBody = MessageFormat.format(VERIFICATION_EMAIL_BODY_TEMPLATE, params);

		return  verificationBody;
	}
}
