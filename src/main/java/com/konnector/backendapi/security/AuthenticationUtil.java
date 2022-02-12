package com.konnector.backendapi.security;

import org.springframework.security.core.Authentication;

public class AuthenticationUtil {

	public static Long getUserId(Authentication authentication) {
		return ((SecurityUser) authentication.getPrincipal()).getUserId();
	}
}
