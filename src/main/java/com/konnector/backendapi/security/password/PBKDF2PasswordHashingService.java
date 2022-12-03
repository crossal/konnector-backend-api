package com.konnector.backendapi.security.password;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;

@Service
public class PBKDF2PasswordHashingService implements PasswordHashingService {

	private static final Logger logger = LogManager.getLogger(PBKDF2PasswordHashingService.class);

	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	// The following constants may be changed without breaking existing hashes.
	private static final int SALT_BYTES = 16;
	private static final int HASH_BYTES = 16;
	private static final int PBKDF2_ITERATIONS = 400000;

	@Autowired
	private SecureRandom secureRandom;
	@Autowired
	private SecretKeyFactoryWrapperService secretKeyFactoryWrapperService;

	@Override
	public HashedPassword getHashedPassword(String password) {
		byte[] salt = new byte[SALT_BYTES];
		secureRandom.nextBytes(salt);

		return getHashedPassword(password, salt);
	}

	@Override
	public HashedPassword getHashedPassword(String password, byte[] salt) {
		char[] passwordChars = password.toCharArray();

		PBEKeySpec pbeKeySpec = new PBEKeySpec(passwordChars, salt, PBKDF2_ITERATIONS, HASH_BYTES * 8);

		try {
			SecretKeyFactoryWrapper secretKeyFactoryWrapper = secretKeyFactoryWrapperService.getInstance(PBKDF2_ALGORITHM);
			byte[] hashedPassword = secretKeyFactoryWrapper.generateSecret(pbeKeySpec).getEncoded();

			return new HashedPassword(hashedPassword, salt, PBKDF2_ITERATIONS);
		} catch (Exception e) {
			logger.error("Exception getting hashed password: '{}'", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hashesEqual(byte[] hashA, byte[] hashB) {
		return slowEquals(hashA, hashB);
	}

	@Override
	public boolean passwordMatchesHash(String password, byte[] salt, byte[] hash) {
		HashedPassword hashedInputPassword = getHashedPassword(password, salt);
		return hashesEqual(hashedInputPassword.getHash(), hash);
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

		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}

		return diff == 0;
	}
}
