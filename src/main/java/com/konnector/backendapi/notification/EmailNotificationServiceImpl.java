package com.konnector.backendapi.notification;

import com.konnector.backendapi.exceptions.EmailVerificationSendException;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Properties;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

	private static final Logger LOGGER = LogManager.getLogger(EmailNotificationServiceImpl.class);

	private static final String FROM_ADDRESS = "support@konnector.io";
	private static final String FROM_NAME = "Konnector";
	private static final String PROPERTY_KEY_HOST = "mail.smtp.host";
	private static final String HOST = "smtp.gmail.com";
	private static final String PROPERTY_KEY_PORT = "mail.smtp.port";
	private static final String PORT = "587";
	private static final String PROPERTY_KEY_STARTTLS = "mail.smtp.starttls.enable";
	private static final String STARTTLS = "true";
	private static final String PROPERTY_KEY_AUTH = "mail.smtp.auth";
	private static final String AUTH = "true";
	private static final String VERIFICATION_EMAIL_SUBJECT = "Verify your email address";
	private static final String VERIFICATION_URL_TEMPLATE = "konnector.io/verifications/verify?type=0&token={0}&username-or-email={1}";
	private static final String VERIFICATION_EMAIL_BODY_TEMPLATE = "Verification code: {0}\nVerification link: {1}";
	private static final String RESET_PASSWORD_EMAIL_SUBJECT = "Reset your password";
	private static final String RESET_PASSWORD_URL_TEMPLATE = "konnector.io/verifications/verify?type=1&token={0}";
	private static final String RESET_PASSWORD_EMAIL_BODY_TEMPLATE = "Password Reset code: {0}\nPassword Reset link: {1}";

	@Autowired
	private EmailTransportWrapper emailTransportWrapper;

	@Value("${email.username}")
	private String emailUserName;
	@Value("${email.password}")
	private String emailPassword;

	private Properties properties;

	@PostConstruct
	public void init() {
		properties = System.getProperties();
		properties.setProperty(PROPERTY_KEY_HOST, HOST);
		properties.setProperty(PROPERTY_KEY_PORT, PORT);
		properties.setProperty(PROPERTY_KEY_AUTH, AUTH);
		properties.setProperty(PROPERTY_KEY_STARTTLS, STARTTLS);
	}

	@Override
	public void sendVerificationEmail(String recipient, String code, String urlToken) {
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUserName, emailPassword);
			}
		});

		MimeMessage message = new MimeMessage(session);

		String verificationUrl = getVerificationUrl(urlToken, recipient);
		String verificationBody = getVerificationBody(code, verificationUrl);

		try {
			message.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(VERIFICATION_EMAIL_SUBJECT);
			message.setText(verificationBody);

			emailTransportWrapper.send(message);
		} catch (Exception e) {
			LOGGER.error("Verification email could not be sent with exception: {}", e);
			throw new EmailVerificationSendException("Verification email could not be sent.");
		}
	}

	@Override
	public void sendPasswordResetEmail(String recipient, String code, String urlToken) {
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailUserName, emailPassword);
			}
		});

		MimeMessage message = new MimeMessage(session);

		String passwordResetUrl = getPasswordResetUrl(urlToken);
		String passwordResetBody = getPasswordResetBody(code, passwordResetUrl);

		try {
			message.setFrom(new InternetAddress(FROM_ADDRESS, FROM_NAME));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(RESET_PASSWORD_EMAIL_SUBJECT);
			message.setText(passwordResetBody);

			emailTransportWrapper.send(message);
		} catch (Exception e) {
			LOGGER.error("Password Reset email could not be sent with exception: {}", e);
			throw new EmailVerificationSendException("Password Reset email could not be sent.");
		}
	}

	private String getVerificationUrl(String urlToken, String recipient) {
		Object[] params = new Object[]{urlToken, recipient};
		String verificationUrl = MessageFormat.format(VERIFICATION_URL_TEMPLATE, params);

		return  verificationUrl;
	}

	private String getVerificationBody(String code, String verificationUrl) {
		Object[] params = new Object[]{code, verificationUrl};
		String verificationBody = MessageFormat.format(VERIFICATION_EMAIL_BODY_TEMPLATE, params);

		return  verificationBody;
	}

	private String getPasswordResetUrl(String urlToken) {
		Object[] params = new Object[]{urlToken};
		String verificationUrl = MessageFormat.format(RESET_PASSWORD_URL_TEMPLATE, params);

		return  verificationUrl;
	}

	private String getPasswordResetBody(String code, String verificationUrl) {
		Object[] params = new Object[]{code, verificationUrl};
		String verificationBody = MessageFormat.format(RESET_PASSWORD_EMAIL_BODY_TEMPLATE, params);

		return  verificationBody;
	}
}
