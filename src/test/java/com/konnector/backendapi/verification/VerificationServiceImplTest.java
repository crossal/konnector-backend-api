package com.konnector.backendapi.verification;

import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.exceptions.InvalidVerificationCodeException;
import com.konnector.backendapi.exceptions.NoVerificationAttemptsLeftException;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceImplTest {

	private VerificationService verificationService;

	@Mock
	private Dao<Verification> verificationDaoMock;
	@Mock
	private VerificationRepository verificationRepositoryMock;
	@Mock
	private Dao<User> userDaoMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private CodeGenerationService codeGenerationServiceMock;

	@Mock
	private User userMock;
	@Mock
	private Verification verificationMock;

	private final long userId = 1;
	private final String code = "code";
	private final String username = "username";
	private final String urlToken = "urlToken";

	@Captor
	ArgumentCaptor<Verification> verificationCaptor;

	@BeforeEach
	public void setup() {
		verificationService = new VerificationServiceImpl(verificationDaoMock, verificationRepositoryMock, userDaoMock, userRepositoryMock, codeGenerationServiceMock);
	}

	@Test
	public void createVerificationForUser_createsVerification() {
		when(codeGenerationServiceMock.generateCode(anyInt())).thenReturn(code);

		Verification verification = verificationService.createEmailVerificationForUser(userId);

		verify(codeGenerationServiceMock, times(1)).generateCode(anyInt());
		verify(verificationDaoMock, times(1)).save(any(Verification.class));
		assertEquals(userId, verification.getUserId());
		assertEquals(VerificationType.EMAIL, verification.getType());
		assertEquals(code, verification.getCode());
		assertNotNull(verification.getUrlToken());
		assertTrue(verification.getCodeAttemptsLeft() > 0);
		assertEquals(VerificationStatus.INCOMPLETE, verification.getStatus());
		assertNotNull(verification.getExpiresOn());
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
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByUrlToken(username, urlToken));
	}

	@Test
	public void verifyEmailByUrlToken_verificationExpired_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().minusDays(2));

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByUrlToken(username, urlToken));
	}

	@Test
	public void verifyEmailByUrlToken_verifiesEmail() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));

		verificationService.verifyEmailByUrlToken(username, urlToken);

		verify(userDaoMock).update(userMock);
		verify(verificationDaoMock).update(verificationMock);
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
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.COMPLETE);

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_verificationExpired_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().minusDays(2));

		assertThrows(InvalidDataException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_noAttemptsLeft_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(0);

		assertThrows(NoVerificationAttemptsLeftException.class, () -> verificationService.verifyEmailByCode(username, code));
	}

	@Test
	public void verifyEmailByCode_codeInvalid_throwsException() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCode()).thenReturn("some_other_code");
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(5);

		assertThrows(InvalidVerificationCodeException.class, () -> verificationService.verifyEmailByCode(username, code));

		verify(verificationDaoMock, times(1)).update(verificationCaptor.capture());
		verify(verificationCaptor.getValue(), times(1)).setCodeAttemptsLeft(4);
	}

	@Test
	public void verifyEmailByCode_verifiesEmail() {
		when(userMock.getId()).thenReturn(userId);
		when(userRepositoryMock.findByEmailOrUsername(username, username)).thenReturn(Optional.of(userMock));
		when(verificationRepositoryMock.findFirstByUserIdAndTypeOrderByCreatedOnDesc(eq(userId), any(VerificationType.class))).thenReturn(Optional.of(verificationMock));
		when(verificationMock.getStatus()).thenReturn(VerificationStatus.INCOMPLETE);
		when(verificationMock.getExpiresOn()).thenReturn(LocalDateTime.now().plusDays(2));
		when(verificationMock.getCode()).thenReturn(code);
		when(verificationMock.getCodeAttemptsLeft()).thenReturn(5);

		verificationService.verifyEmailByCode(username, code);

		verify(userDaoMock).update(userMock);
		verify(verificationDaoMock).update(verificationMock);
	}
}
