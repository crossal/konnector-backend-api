package com.konnector.backendapi.verification;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationController {

	@GetMapping(value = "/verifications/verify", params = {"token", "type=1"})
	@ModelAttribute
	public String resetPassword(@RequestParam String token, @RequestParam Integer type, Model model) {
		model.addAttribute("passwordResetToken", token);
		return "verify";
	}
}
