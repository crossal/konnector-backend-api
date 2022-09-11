package com.konnector.backendapi.security.password;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class SecretKeyFactoryWrapper {
	private SecretKeyFactory secretKeyFactory;

	public SecretKeyFactoryWrapper(SecretKeyFactory secretKeyFactory) {
		this.secretKeyFactory = secretKeyFactory;
	}

	public static SecretKeyFactoryWrapper getInstance(String algorithm) throws NoSuchAlgorithmException {
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
		SecretKeyFactoryWrapper secretKeyFactoryWrapper = new SecretKeyFactoryWrapper(secretKeyFactory);
		return secretKeyFactoryWrapper;
	}

	public static SecretKeyFactoryWrapper getInstance(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException {
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm, provider);
		SecretKeyFactoryWrapper secretKeyFactoryWrapper = new SecretKeyFactoryWrapper(secretKeyFactory);
		return secretKeyFactoryWrapper;
	}

	public static SecretKeyFactoryWrapper getInstance(String algorithm, Provider provider) throws NoSuchAlgorithmException {
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm, provider);
		SecretKeyFactoryWrapper secretKeyFactoryWrapper = new SecretKeyFactoryWrapper(secretKeyFactory);
		return secretKeyFactoryWrapper;
	}

	public Provider getProvider() {
		return secretKeyFactory.getProvider();
	}

	public String getAlgorithm() {
		return secretKeyFactory.getAlgorithm();
	}

	public SecretKey generateSecret(KeySpec keySpec) throws InvalidKeySpecException {
		return secretKeyFactory.generateSecret(keySpec);
	}

	public KeySpec getKeySpec(SecretKey key, Class<?> keySpec) throws InvalidKeySpecException {
		return secretKeyFactory.getKeySpec(key, keySpec);
	}

	public SecretKey translateKey(SecretKey key) throws InvalidKeyException {
		return secretKeyFactory.translateKey(key);
	}
}
