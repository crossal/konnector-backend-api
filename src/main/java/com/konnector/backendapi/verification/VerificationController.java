package com.konnector.backendapi.verification;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {

	@Autowired
	private VerificationService verificationService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(value = "/api/verifications/verify", params = "token")
	@ResponseStatus(HttpStatus.OK)
	public void verifyUserEmailByToken(@RequestParam String usernameOrEmail, @RequestParam String token) {
		verificationService.verifyEmailByUrlToken(usernameOrEmail, token);
	}

	@PostMapping(value = "/api/verifications/verify", params = "code")
	@ResponseStatus(HttpStatus.OK)
	public void verifyUserEmailByCode(@RequestParam String usernameOrEmail, @RequestParam String code) {
		verificationService.verifyEmailByCode(usernameOrEmail, code);
	}
}
