package com.konnector.backendapi.verification;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {

	@Autowired
	private VerificationService verificationService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping(value = "/api/verifications", params = { "usernameOrEmail", "type=0" })
	@ResponseStatus(HttpStatus.OK)
	public void createEmailVerificationForUser(@RequestParam String usernameOrEmail, @RequestParam Integer type) {
		verificationService.createEmailVerificationForUser(usernameOrEmail);
	}

	@PostMapping(value = "/api/verifications/verify", params = { "token", "type=0" })
	@ResponseStatus(HttpStatus.OK)
	public void verifyUserEmailByToken(@RequestParam String usernameOrEmail, @RequestParam String token, @RequestParam Integer type) {
		verificationService.verifyEmailByUrlToken(usernameOrEmail, token);
	}

	@PostMapping(value = "/api/verifications/verify", params = "type=0")
	@ResponseStatus(HttpStatus.OK)
	public void verifyUserEmailByCode(@RequestBody VerificationDTO verificationDTO, @RequestParam Integer type) {
		verificationService.verifyEmailByCode(verificationDTO.getUsernameOrEmail(), verificationDTO.getCode());
	}

	@PostMapping(value = "/api/verifications", params = { "usernameOrEmail", "type=1" })
	@ResponseStatus(HttpStatus.OK)
	public void createPasswordResetForUser(@RequestParam String usernameOrEmail, @RequestParam Integer type) {
		verificationService.createPasswordResetForUser(usernameOrEmail);
	}

	@PostMapping(value = "/api/verifications/verify", params = {"passwordResetToken", "type=1"})
	@ResponseStatus(HttpStatus.OK)
	public void resetUserPasswordWithToken(@RequestBody VerificationDTO verificationDTO, @RequestParam String passwordResetToken, @RequestParam Integer type) {
		verificationService.resetPasswordWithToken(verificationDTO.getUsernameOrEmail(), verificationDTO.getUserPassword(), passwordResetToken);
	}

	@PostMapping(value = "/api/verifications/verify", params = {"type=1"})
	@ResponseStatus(HttpStatus.OK)
	public void resetUserPasswordWithCode(@RequestBody VerificationDTO verificationDTO, @RequestParam Integer type) {
		verificationService.resetPasswordWithCode(verificationDTO.getUsernameOrEmail(), verificationDTO.getUserPassword(), verificationDTO.getCode());
	}
}
