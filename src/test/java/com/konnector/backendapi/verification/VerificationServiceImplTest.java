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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceImplTest {

	private VerificationService verificationService;

	@Mock
	private VerificationRepository verificationRepositoryMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private CodeGenerationService codeGenerationServiceMock;
	@Mock
	private EmailNotificationService emailNotificationServiceMock;
	@Mock
	private UserService userServiceMock;

	@Mock
	private User userMock;
	@Mock
	private Verification verificationMock;

	private final static long userId = 1;
	private final static String code = "code";
	private final static String username = "username";
	private final static String urlToken = "urlToken";
	private final static String email = "email";
	private final static String password = "password";

	@Captor
	private ArgumentCaptor<Verification> verificationCaptor;

	@BeforeEach
	public void setup() {
		verificationService = new VerificationServiceImpl(verificationRepositoryMock,
				userRepositoryMock, codeGenerationServiceMock, emailNotificationServiceMock,
				userServiceMock);
	}

	@Test
	public void createVerificationForUser_withUsernameOrEmail_createsVerification() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));

		when(userMock.getId()).thenReturn(userId);
		when(codeGenerationServiceMock.generateCode(anyInt())).thenReturn(code);
		when(userMock.getEmail()).thenReturn(email);

		Verification verification = verificationService.createEmailVerificationForUser(username);

		verify(codeGenerationServiceMock).generateCode(anyInt());
		verify(verificationRepositoryMock).save(verification);
		verify(emailNotificationServiceMock).sendVerificationEmail(eq(email), eq(code), anyString());
		assertEquals(userId, verification.getUserId());
		assertEquals(VerificationType.EMAIL, verification.getType());
		assertEquals(code, verification.getCode());
		assertNotNull(verification.getUrlToken());
		assertTrue(verification.getCodeAttemptsLeft() > 0);
		assertEquals(VerificationStatus.INCOMPLETE, verification.getStatus());
		assertNotNull(verification.getExpiresOn());
	}

	@Test
	public void createVerificationForUser_withUsernameOrEmailAndUserNotFound_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> verificationService.createEmailVerificationForUser(username));
	}

	@Test
	public void createVerificationForUser_withUsernameOrEmailAndExistingVerification_createsVerification() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getReverifyAllowedOn()).thenReturn(LocalDateTime.now().minusDays(1));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(userMock.getId()).thenReturn(userId);
		when(codeGenerationServiceMock.generateCode(anyInt())).thenReturn(code);
		when(userMock.getEmail()).thenReturn(email);

		Verification verification = verificationService.createEmailVerificationForUser(username);

		verify(codeGenerationServiceMock).generateCode(anyInt());
		verify(verificationRepositoryMock).save(any(Verification.class));
		verify(emailNotificationServiceMock).sendVerificationEmail(eq(email), eq(code), anyString());
		assertEquals(userId, verification.getUserId());
		assertEquals(VerificationType.EMAIL, verification.getType());
		assertEquals(code, verification.getCode());
		assertNotNull(verification.getUrlToken());
		assertTrue(verification.getCodeAttemptsLeft() > 0);
		assertEquals(VerificationStatus.INCOMPLETE, verification.getStatus());
		assertNotNull(verification.getExpiresOn());
	}

	@Test
	public void createVerificationForUser_withUsernameOrEmailAndExistingVerificationAlreadyVerified_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.createEmailVerificationForUser(username));
	}

	@Test
	public void createVerificationForUser_withUsernameOrEmailAndExistingVerificationAndNotAllowedReverifyYet_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getReverifyAllowedOn()).thenReturn(LocalDateTime.now().plusDays(1));

		assertThrows(InvalidDataException.class, () -> verificationService.createEmailVerificationForUser(username));
	}

	@Test
	public void createVerificationForUser_withUser_createsVerification() {
		when(userMock.getId()).thenReturn(userId);
		when(codeGenerationServiceMock.generateCode(anyInt())).thenReturn(code);
		when(userMock.getEmail()).thenReturn(email);

		Verification verification = verificationService.createEmailVerificationForUser(userMock);

		verify(codeGenerationServiceMock).generateCode(anyInt());
		verify(verificationRepositoryMock).save(any(Verification.class));
		verify(emailNotificationServiceMock).sendVerificationEmail(eq(email), eq(code), anyString());
		assertEquals(userId, verification.getUserId());
		assertEquals(VerificationType.EMAIL, verification.getType());
		assertEquals(code, verification.getCode());
		assertNotNull(verification.getUrlToken());
		assertTrue(verification.getCodeAttemptsLeft() > 0);
		assertEquals(VerificationStatus.INCOMPLETE, verification.getStatus());
		assertNotNull(verification.getExpiresOn());
	}

	@Test
	public void createVerificationForUser_withUserAndExistingVerification_createsVerification() {
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getReverifyAllowedOn()).thenReturn(LocalDateTime.now().minusDays(1));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(userMock.getId()).thenReturn(userId);
		when(codeGenerationServiceMock.generateCode(anyInt())).thenReturn(code);
		when(userMock.getEmail()).thenReturn(email);

		Verification verification = verificationService.createEmailVerificationForUser(userMock);

		verify(codeGenerationServiceMock).generateCode(anyInt());
		verify(verificationRepositoryMock).save(any(Verification.class));
		verify(emailNotificationServiceMock).sendVerificationEmail(eq(email), eq(code), anyString());
		assertEquals(userId, verification.getUserId());
		assertEquals(VerificationType.EMAIL, verification.getType());
		assertEquals(code, verification.getCode());
		assertNotNull(verification.getUrlToken());
		assertTrue(verification.getCodeAttemptsLeft() > 0);
		assertEquals(VerificationStatus.INCOMPLETE, verification.getStatus());
		assertNotNull(verification.getExpiresOn());
	}

	@Test
	public void createVerificationForUser_withUserAndExistingVerificationAlreadyVerified_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.createEmailVerificationForUser(userMock));
	}

	@Test
	public void createVerificationForUser_withUserAndExistingVerificationAndNotAllowedReverifyYet_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getReverifyAllowedOn()).thenReturn(LocalDateTime.now().plusDays(1));

		assertThrows(InvalidDataException.class, () -> verificationService.createEmailVerificationForUser(userMock));
	}

	@Test
	public void verifyEmailByUrlToken_userNotFound_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByUrlToken(username, urlToken));
	}

	@Test
	public void verifyEmailByUrlToken_verificationNotFound_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(any(), any())).thenReturn(Optional.empty());

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByUrlToken(username, urlToken));
	}

	@Test
	public void verifyEmailByUrlToken_verificationAlreadyComplete_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByUrlToken(username, urlToken));
	}

	@Test
	public void verifyEmailByUrlToken_verificationExpired_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().minusDays(2));

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByUrlToken(username, urlToken));
	}

	@Test
	public void verifyEmailByUrlToken_invalidToken_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));

		assertThrows(InvalidVerificationCodeException.class, () -> verificationService.verifyEmailByUrlToken(username, urlToken));
	}

	@Test
	public void verifyEmailByUrlToken_verifiesEmail() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getUrlToken()).thenReturn(urlToken);

		verificationService.verifyEmailByUrlToken(username, urlToken);

		verify(userRepositoryMock).save(userMock);
		verify(verificationRepositoryMock).save(verificationMock);
	}

	@Test
	public void verifyEmailByCode_userNotFound_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(any(), any())).thenReturn(Optional.empty());

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_verificationNotFound_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(any(), any())).thenReturn(Optional.empty());

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_verificationAlreadyComplete_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_verificationExpired_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().minusDays(2));

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_noAttemptsLeft_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(0);

		assertThrows(NoVerificationAttemptsLeftException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_codeInvalid_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCode()).thenReturn("some_other_code");
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(5);

		assertThrows(InvalidVerificationCodeException.class, () -> verificationService.verifyEmailByCode(username, code));

		verify(verificationRepositoryMock).save(verificationCaptor.capture());
		verify(verificationCaptor.getValue()).setCodeAttemptsLeft(4);
	}

	@Test
	public void verifyEmailByCode_verifiesEmail() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.EMAIL)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCode()).thenReturn(code);
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(5);

		verificationService.verifyEmailByCode(username, code);

		verify(userRepositoryMock).save(userMock);
		verify(verificationMock).setStatus(VerificationStatus.COMPLETE);
		verify(verificationRepositoryMock).save(verificationMock);
	}

	@Test
	public void createPasswordResetForUser_userNotFound_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.createPasswordResetForUser(username));
	}

	@Test
	public void createPasswordResetForUser_existingPasswordResetAndNotAllowedReverifyYet_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getReverifyAllowedOn()).thenReturn(LocalDateTime.now().plusDays(1));

		assertThrows(InvalidDataException.class, () -> verificationService.createPasswordResetForUser(username));
	}

	@Test
	public void createPasswordResetForUser_createsPasswordReset() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getReverifyAllowedOn()).thenReturn(LocalDateTime.now().minusDays(1));
		when(codeGenerationServiceMock.generateCode(anyInt())).thenReturn(code);
		when(userMock.getEmail()).thenReturn(email);

		Verification passwordReset = verificationService.createPasswordResetForUser(username);

		verify(codeGenerationServiceMock).generateCode(anyInt());
		verify(verificationRepositoryMock).save(passwordReset);
		verify(emailNotificationServiceMock).sendPasswordResetEmail(email, passwordReset.getCode(), passwordReset.getUrlToken());
		assertEquals(userId, passwordReset.getUserId());
		assertEquals(VerificationType.PASSWORD, passwordReset.getType());
		assertEquals(code, passwordReset.getCode());
		assertNotNull(passwordReset.getUrlToken());
		assertTrue(passwordReset.getCodeAttemptsLeft() > 0);
		assertEquals(VerificationStatus.INCOMPLETE, passwordReset.getStatus());
		assertNotNull(passwordReset.getExpiresOn());
	}

	@Test
	public void resetPasswordWithToken_nullToken_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, password, null));
	}

	@Test
	public void resetPasswordWithToken_emptyToken_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, password, ""));
	}

	@Test
	public void resetPasswordWithToken_nullUsernameOrEmail_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(null, password, urlToken));
	}

	@Test
	public void resetPasswordWithToken_emptyUsernameOrEmail_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken("", password, urlToken));
	}

	@Test
	public void resetPasswordWithToken_nullPassword_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, null, urlToken));
	}

	@Test
	public void resetPasswordWithToken_emptyPassword_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, "", urlToken));
	}

	@Test
	public void resetPasswordWithToken_userNotFound_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, password, urlToken));
	}

	@Test
	public void resetPasswordWithToken_verificationNotFound_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);

		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, password, urlToken));
	}

	@Test
	public void resetPasswordWithToken_resetAlreadyComplete_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, password, urlToken));
	}

	@Test
	public void resetPasswordWithToken_resetExpired_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().minusDays(2));

		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithToken(username, password, urlToken));
	}

	@Test
	public void resetPasswordWithToken_invalidToken_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));

		assertThrows(InvalidVerificationCodeException.class, () -> verificationService.resetPasswordWithToken(username, password, urlToken));
	}

	@Test
	public void resetPasswordWithToken_resetsPassword() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getUrlToken()).thenReturn(urlToken);

		verificationService.resetPasswordWithToken(username, password, urlToken);

		verify(userServiceMock).updateUserPassword(userMock, password);
		verify(verificationMock).setStatus(VerificationStatus.COMPLETE);
		verify(verificationRepositoryMock).save(verificationMock);
	}

	@Test
	public void resetPasswordWithCode_nullCode_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, password, null));
	}

	@Test
	public void resetPasswordWithCode_emptyCode_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, password, ""));
	}

	@Test
	public void resetPasswordWithCode_nullUsernameOrEmail_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(null, password, code));
	}

	@Test
	public void resetPasswordWithCode_emptyUsernameOrEmail_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode("", password, code));
	}

	@Test
	public void resetPasswordWithCode_nullPassword_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, null, code));
	}

	@Test
	public void resetPasswordWithCode_emptyPassword_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, "", code));
	}

	@Test
	public void resetPasswordWithCode_userNotFound_throwsException() {
		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, password, code));
	}

	@Test
	public void resetPasswordWithCode_verificationNotFound_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);

		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, password, code));
	}

	@Test
	public void resetPasswordWithCode_resetAlreadyComplete_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, password, code));
	}

	@Test
	public void resetPasswordWithCode_resetExpired_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().minusDays(2));

		assertThrows(InvalidDataException.class, () -> verificationService.resetPasswordWithCode(username, password, code));
	}

	@Test
	public void resetPasswordWithCode_noAttemptsLeft_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));

		assertThrows(NoVerificationAttemptsLeftException.class, () -> verificationService.resetPasswordWithCode(username, password, code));
	}

	@Test
	public void resetPasswordWithCode_codeInvalid_throwsException() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(1);

		assertThrows(InvalidVerificationCodeException.class, () -> verificationService.resetPasswordWithCode(username, password, code));
	}

	@Test
	public void resetPasswordWithCode_resetsPassword() {
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(userMock.getId()).thenReturn(userId);
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(userId, VerificationType.PASSWORD)).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(1);
		when(verificationMock.getCode()).thenReturn(code);

		verificationService.resetPasswordWithCode(username, password, code);

		verify(userServiceMock).updateUserPassword(userMock, password);
		verify(verificationMock).setStatus(VerificationStatus.COMPLETE);
		verify(verificationRepositoryMock).save(verificationMock);
	}
}
