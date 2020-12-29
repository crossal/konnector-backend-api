package com.konnector.backendapi.security.password;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PBKDF2PasswordEncoder implements PasswordEncoder {



	@Override
	public boolean matches(CharSequence charSequence, String s) {
		return false;
	}

	@Override
	public String encode(CharSequence charSequence) {
		return null;
	}
}
