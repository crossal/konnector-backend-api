package com.konnector.backendapi.security.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PBKDf2PasswordHashingServiceTest {

	@InjectMocks
	private PasswordHashingService passwordHashingService = new PBKDF2PasswordHashingService();

	@Mock
	private SecureRandom secureRandomMock;
	@Mock
	private SecretKeyFactoryWrapperService secretKeyFactoryWrapperServiceMock;
	@Mock
	private SecretKeyFactory secretKeyFactoryMock;
	@Mock
	private SecretKey secretKeyMock;

	private final String password = "password";
	private final byte[] secret = new byte[16];
	private final Random random = new Random();

	@Captor
	private ArgumentCaptor<PBEKeySpec> pbeKeySpecArgumentCaptor;

	@BeforeEach
	public void setup() throws NoSuchAlgorithmException, InvalidKeySpecException {
		random.nextBytes(secret);
//		when(secretKeyFactoryWrapperServiceMock.getInstance(anyString())).thenReturn(secretKeyFactoryMock);
//		when(secretKeyFactoryMock.generateSecret(any(KeySpec.class))).thenReturn(secretKeyMock);
//		when(secretKeyMock.getEncoded()).thenReturn(secret);
	}

	@Test
	public void getHashedPassword_returnsHashedPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
		when(secretKeyFactoryWrapperServiceMock.getInstance(anyString())).thenReturn(secretKeyFactoryMock);
		when(secretKeyFactoryMock.generateSecret(any(KeySpec.class))).thenReturn(secretKeyMock);
		when(secretKeyMock.getEncoded()).thenReturn(secret);

		HashedPassword hashedPassword = passwordHashingService.getHashedPassword(password);
		verify(secureRandomMock, times(1)).nextBytes(any());
		verify(secretKeyFactoryMock, times(1)).generateSecret(pbeKeySpecArgumentCaptor.capture());
		assertEquals(password, new String(pbeKeySpecArgumentCaptor.getValue().getPassword()));
		assertNotNull(hashedPassword);
		assertNotEquals(password, hashedPassword.getHash());
		assertNotNull(hashedPassword.getSalt());
	}

	@Test
	public void getHashedPasswordWithSalt_returnsHashedPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		when(secretKeyFactoryWrapperServiceMock.getInstance(anyString())).thenReturn(secretKeyFactoryMock);
		when(secretKeyFactoryMock.generateSecret(any(KeySpec.class))).thenReturn(secretKeyMock);
		when(secretKeyMock.getEncoded()).thenReturn(secret);

		HashedPassword hashedPassword = passwordHashingService.getHashedPassword(password, salt);
		verify(secureRandomMock, times(0)).nextBytes(any());
		verify(secretKeyFactoryMock, times(1)).generateSecret(pbeKeySpecArgumentCaptor.capture());
		assertEquals(password, new String(pbeKeySpecArgumentCaptor.getValue().getPassword()));
		assertNotNull(hashedPassword);
		assertNotEquals(password, hashedPassword.getHash());
		assertNotNull(hashedPassword.getSalt());
	}

	@Test
	public void hashesEqual_hashesEqual_returnsTrue() {
		assertTrue(passwordHashingService.hashesEqual(secret, secret));
	}

	@Test
	public void hashesEqual_hashesDoNotEqual_returnsFalse() {
		byte[] otherHash = new byte[16];
		random.nextBytes(otherHash);
		assertFalse(passwordHashingService.hashesEqual(secret, otherHash));
	}

	@Test
	public void passwordMatchesHash_hashesEqual_returnsTrue() throws NoSuchAlgorithmException, InvalidKeySpecException {
		when(secretKeyFactoryWrapperServiceMock.getInstance(anyString())).thenReturn(secretKeyFactoryMock);
		when(secretKeyFactoryMock.generateSecret(any(KeySpec.class))).thenReturn(secretKeyMock);
		when(secretKeyMock.getEncoded()).thenReturn(secret);

		assertTrue(passwordHashingService.passwordMatchesHash(password, new byte[16], secret));
	}

	@Test
	public void passwordMatchesHash_hashesDoNotEqual_returnsFalse() throws NoSuchAlgorithmException, InvalidKeySpecException {
		when(secretKeyFactoryWrapperServiceMock.getInstance(anyString())).thenReturn(secretKeyFactoryMock);
		when(secretKeyFactoryMock.generateSecret(any(KeySpec.class))).thenReturn(secretKeyMock);
		when(secretKeyMock.getEncoded()).thenReturn(secret);

		assertFalse(passwordHashingService.passwordMatchesHash(password, new byte[16], new byte[16]));
	}
}
