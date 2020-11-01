package com.konnector.backendapi.security;

public class HashedPassword {
	private byte[] hash;
	private byte[] salt;
	private int interations;

	public HashedPassword(byte[] hash, byte[] salt, int iterations) {
		this.hash = hash;
		this.salt = salt;
		this.interations = iterations;
	}

	public byte[] getHash() {
		return hash;
	}

	public byte[] getSalt() {
		return salt;
	}
}
