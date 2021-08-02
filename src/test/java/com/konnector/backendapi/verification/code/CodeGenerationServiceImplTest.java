package com.konnector.backendapi.verification.code;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeGenerationServiceImplTest {

	private CodeGenerationService codeGenerationService = new CodeGenerationServiceImpl();

	private final int length = 5;

	@Test
	public void generateCode_generatesCode() {
		String code = codeGenerationService.generateCode(length);

		assertEquals(length, code.length());
		Integer.parseInt(code);
	}
}
