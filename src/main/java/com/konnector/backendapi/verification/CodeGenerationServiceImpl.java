package com.konnector.backendapi.verification;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {

	@Override
	public String generateCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(ThreadLocalRandom.current().nextInt(0, 10));
		}
		return sb.toString();
	}
}
