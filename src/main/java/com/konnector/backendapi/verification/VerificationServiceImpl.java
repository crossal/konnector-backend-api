package com.konnector.backendapi.verification;

import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.exceptions.InvalidVerificationCodeException;
import com.konnector.backendapi.exceptions.NoVerificationAttemptsLeftException;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.notification.EmailNotificationService;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserRepository;
import com.konnector.backendapi.user.UserService;
import com.konnector.backendapi.verification.code.CodeGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationServiceImpl implements VerificationService {

	private static final int CODE_LENGTH = 4;
	private static final int CODE_ATTEMPTS = 5;
	private static final int URL_TOKEN_EXPIRATION_IN_DAYS = 5;
	private static final int RESEND_ALLOWED_IN_MINUTES = 30;

	@Autowired
	private VerificationRepository verificationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CodeGenerationService codeGenerationService;
	@Autowired
	private EmailNotificationService emailNotificationService;
	@Autowired
	private UserService userService;

	public VerificationServiceImpl(VerificationRepository verificationRepository, UserRepository userRepository,
	                               CodeGenerationService codeGenerationService, EmailNotificationService emailNotificationService,
	                               UserService userService) {
		this.verificationRepository = verificationRepository;
		this.userRepository = userRepository;
		this.codeGenerationService = codeGenerationService;
		this.emailNotificationService = emailNotificationService;
		this.userService = userService;
	}

	@Override
	@Transactional
	public Verification createEmailVerificationForUser(User user) {
		Optional<Verification> optionalExistingVerification = verificationRepository.findFirstByUserIdAndTypeOrderByCreatedOnDesc(user.getId(), VerificationType.EMAIL);
		optionalExistingVerification.ifPresent(verification -> {
			if (verification.getStatus().equals(VerificationStatus.COMPLETE)) {
				throw new InvalidDataException("Already verified.");
			}

			if (verification.getReverifyAllowedOn().isAfter(LocalDateTime.now())) {
				throw new InvalidDataException("Verification resend not allowed so soon.");
			}
		});

		Verification verification = new Verification(user.getId(), VerificationType.EMAIL, codeGenerationService.generateCode(CODE_LENGTH), CODE_ATTEMPTS,
				UUID.randomUUID().toString(), LocalDateTime.now().plusDays(URL_TOKEN_EXPIRATION_IN_DAYS), LocalDateTime.now().plusMinutes(RESEND_ALLOWED_IN_MINUTES));
		verificationRepository.save(verification);

		emailNotificationService.sendVerificationEmail(user.getEmail(), verification.getCode(), verification.getUrlToken());

		return verification;
	}

	@Override
	@Transactional
	public Verification createEmailVerificationForUser(String usernameOrEmail) {
		Optional<User> optionalUser = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);

		return optionalUser.map(user -> createEmailVerificationForUser(user))
				.orElseThrow(() -> new NotFoundException("User not found."));
	}

	@Override
	@Transactional
	public void verifyEmailByUrlToken(String usernameOrEmail, String urlToken) {
		validateUsernameOrEmail(usernameOrEmail);
		if (urlToken == null || urlToken.isEmpty()) {
			throw new InvalidDataException("URL token cannot be empty.");
		}

		verifyEmailByUrlTokenOrCode(usernameOrEmail, urlToken, false);
	}

	@Override
	@Transactional(noRollbackFor = InvalidVerificationCodeException.class)
	public void verifyEmailByCode(String usernameOrEmail, String code) {
		validateUsernameOrEmail(usernameOrEmail);
		if (code == null || code.isEmpty()) {
			throw new InvalidDataException("Code cannot be empty.");
		}

		verifyEmailByUrlTokenOrCode(usernameOrEmail, code, true);
	}

	private void validateUsernameOrEmail(String usernameOrEmail) {
		if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
			throw new InvalidDataException("Username or Email cannot be empty.");
		}
	}

	private void verifyEmailByUrlTokenOrCode(String usernameOrEmail, String urlTokenOrCode, boolean usingCode) {
		Optional<User> optionalUser = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
		User user = optionalUser.orElseThrow(() -> new InvalidDataException("User not found."));
		Optional<Verification> optionalVerification = verificationRepository.findFirstByUserIdAndTypeOrderByCreatedOnDesc(user.getId(), VerificationType.EMAIL);
		Verification verification = optionalVerification.orElseThrow(() -> new InvalidDataException("No verification found for user."));

		if (verification.getStatus().equals(VerificationStatus.COMPLETE)) {
			throw new InvalidDataException("Already verified.");
		}

		if (verification.getExpiresOn().isBefore(LocalDateTime.now())) {
			throw new InvalidDataException("Verification expired.");
		}

		if (usingCode) {
			if (verification.getCodeAttemptsLeft() == 0) {
				throw new NoVerificationAttemptsLeftException("No verification code attempts left.");
			}

			if (!urlTokenOrCode.equals(verification.getCode())) {
				verification.setCodeAttemptsLeft(verification.getCodeAttemptsLeft() - 1);
				verificationRepository.save(verification);
				throw new InvalidVerificationCodeException("Code incorrect.");
			}
		} else if (!urlTokenOrCode.equals(verification.getUrlToken())) {
			throw new InvalidVerificationCodeException("URL token incorrect.");
		}

		user.setEmailVerified(true);
		userRepository.save(user);

		verification.setStatus(VerificationStatus.COMPLETE);
		verificationRepository.save(verification);
	}

	@Override
	@Transactional
	public Verification createPasswordResetForUser(String usernameOrEmail) {
		User user = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new InvalidDataException("User not found."));

		Optional<Verification> optionalExistingVerification = verificationRepository.findFirstByUserIdAndTypeOrderByCreatedOnDesc(user.getId(), VerificationType.PASSWORD);
		optionalExistingVerification.ifPresent(verification -> {
			if (verification.getReverifyAllowedOn().isAfter(LocalDateTime.now())) {
				throw new InvalidDataException("Reset not allowed so soon.");
			}
		});

		Verification verification = new Verification(user.getId(), VerificationType.PASSWORD, codeGenerationService.generateCode(CODE_LENGTH), CODE_ATTEMPTS,
				UUID.randomUUID().toString(), LocalDateTime.now().plusDays(URL_TOKEN_EXPIRATION_IN_DAYS), LocalDateTime.now().plusMinutes(RESEND_ALLOWED_IN_MINUTES));
		verificationRepository.save(verification);

		emailNotificationService.sendPasswordResetEmail(user.getEmail(), verification.getCode(), verification.getUrlToken());

		return verification;
	}

	@Override
	@Transactional
	public void resetPasswordWithToken(String usernameOrEmail, String userPassword, String passwordResetToken) {
		if (passwordResetToken == null || passwordResetToken.isEmpty()) {
			throw new InvalidDataException("URL token cannot be empty.");
		}

		resetPasswordWithTokenOrCode(usernameOrEmail, userPassword, passwordResetToken, false);
	}

	@Override
	@Transactional
	public void resetPasswordWithCode(String usernameOrEmail, String userPassword, String passwordResetCode) {
		if (passwordResetCode == null || passwordResetCode.isEmpty()) {
			throw new InvalidDataException("Code cannot be empty.");
		}

		resetPasswordWithTokenOrCode(usernameOrEmail, userPassword, passwordResetCode, true);
	}

	private void resetPasswordWithTokenOrCode(String usernameOrEmail, String userPassword, String passwordResetUrlTokenOrCode, boolean usingCode) {
		validateUsernameOrEmail(usernameOrEmail);

		if (userPassword == null || userPassword.isEmpty()) {
			throw new InvalidDataException("Password cannot be empty.");
		}

		Optional<User> optionalUser = userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
		User user = optionalUser.orElseThrow(() -> new InvalidDataException("User not found."));
		Optional<Verification> optionalVerification = verificationRepository.findFirstByUserIdAndTypeOrderByCreatedOnDesc(user.getId(), VerificationType.PASSWORD);
		Verification verification = optionalVerification.orElseThrow(() -> new InvalidDataException("No verification found for user."));

		if (verification.getStatus().equals(VerificationStatus.COMPLETE)) {
			throw new InvalidDataException("Already verified.");
		}

		if (verification.getExpiresOn().isBefore(LocalDateTime.now())) {
			throw new InvalidDataException("Reset expired.");
		}

		if (usingCode) {
			if (verification.getCodeAttemptsLeft() == 0) {
				throw new NoVerificationAttemptsLeftException("No verification code attempts left.");
			}

			if (!passwordResetUrlTokenOrCode.equals(verification.getCode())) {
				verification.setCodeAttemptsLeft(verification.getCodeAttemptsLeft() - 1);
				verificationRepository.save(verification); // not working?
				throw new InvalidVerificationCodeException("Code incorrect.");
			}
		} else if (!passwordResetUrlTokenOrCode.equals(verification.getUrlToken())) {
			throw new InvalidVerificationCodeException("URL token incorrect.");
		}

		userService.updateUserPassword(user, userPassword);

		verification.setStatus(VerificationStatus.COMPLETE);
		verificationRepository.save(verification);
	}
}
