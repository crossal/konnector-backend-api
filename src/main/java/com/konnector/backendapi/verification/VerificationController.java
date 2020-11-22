package com.konnector.backendapi.verification;

import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserDTO;
import com.konnector.backendapi.user.UserService;
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

	@PostMapping("/api/verifications/verifyEmail")
	@ResponseStatus(HttpStatus.OK)
	public void verifyUserByUrlToken(@RequestParam String usernameOrEmail, @RequestParam String token, @RequestParam String code) {
		if (token != null && !token.isEmpty()) {
			verificationService.verifyEmailByUrlToken(usernameOrEmail, token);
		} else if (code != null && !code.isEmpty()) {
			verificationService.verifyEmailByCode(usernameOrEmail, code);
		} else {
			throw new InvalidDataException("Token or code cannot be empty");
		}
	}

	@PostMapping("/api/verifications/verifyEmail")
	@ResponseStatus(HttpStatus.OK)
	public void verifyUserByUrlToken(@RequestParam String usernameOrEmail, @RequestParam String code) {
		verificationService.verifyEmailByCode(usernameOrEmail, code);
	}
}
