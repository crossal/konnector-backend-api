package com.konnector.backendapi.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

@Service
public class PBKDF2PasswordHashingService implements PasswordHashingService {

	private static final Logger logger = LogManager.getLogger(PBKDF2PasswordHashingService.class);

	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	// The following constants may be changed without breaking existing hashes.
	public static final int SALT_BYTES = 16;
	public static final int HASH_BYTES = 16;
	public static final int PBKDF2_ITERATIONS = 400000;

	@Override
	public HashedPassword getHashedPassword(String password) {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTES];
		random.nextBytes(salt);

		byte[] hash = getHashedPassword(password, salt);

		return new HashedPassword(hash, salt, PBKDF2_ITERATIONS);
	}

	@Override
	public boolean isPasswordValid(String password, byte[] hash, byte[] salt) {
		byte[] hashedInputPassword = getHashedPassword(password, salt);
		return slowEquals(hash, hashedInputPassword);
	}

	private byte[] getHashedPassword(String password, byte[] salt) {
		char[] passwordChars = password.toCharArray();

		PBEKeySpec pbeKeySpec = new PBEKeySpec(passwordChars, salt, PBKDF2_ITERATIONS, HASH_BYTES * 8);

		try {
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
			return secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
		} catch (Exception e) {
			logger.error("Exception getting hashed password: '{}'", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Compares two byte arrays in length-constant time. This comparison method
	 * is used so that password hashes cannot be extracted from an on-line
	 * system using a timing attack and then attacked off-line.
	 *
	 * @param   a       the first byte array
	 * @param   b       the second byte array
	 * @return          true if both byte arrays are the same, false if not
	 */
	private boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;

		for(int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}

		return diff == 0;
	}
}
