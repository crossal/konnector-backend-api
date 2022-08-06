package com.konnector.backendapi.health;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/api/health")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getHealth() {
		return "Ok";
	}

	@GetMapping("/api/auth-health")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getAuthHealth() {
		return "Ok";
	}
}
