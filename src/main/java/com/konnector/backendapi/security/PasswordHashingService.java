package com.konnector.backendapi.security;

public interface PasswordHashingService {
	HashedPassword getHashedPassword(String password);
	boolean isPasswordValid(String password, byte[] hash, byte[] salt);
}
