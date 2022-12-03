package com.konnector.backendapi.verification.code;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {

	private SecureRandom secureRandom = new SecureRandom();

	@Override
	public String generateCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(secureRandom.nextInt(0, 10));
		}
		return sb.toString();
	}
}
