package com.konnector.backendapi.security.password;

public interface PasswordHashingService {
	HashedPassword getHashedPassword(String password);
	HashedPassword getHashedPassword(String password, byte[] salt);
	boolean passwordMatchesHash(String password, byte[] salt, byte[] hash);
	boolean hashesEqual(byte[] hashA, byte[] hashB);
}
