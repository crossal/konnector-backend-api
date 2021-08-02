package com.konnector.backendapi.security.password;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;

@Service
public class SecretKeyFactoryWrapperService {

	public SecretKeyFactory getInstance(String algorithm) throws NoSuchAlgorithmException {
		return SecretKeyFactory.getInstance(algorithm);
	}
}
