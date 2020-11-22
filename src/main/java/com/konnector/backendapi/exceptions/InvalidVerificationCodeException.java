package com.konnector.backendapi.exceptions;

public class InvalidVerificationCodeException extends RuntimeException  {

	public InvalidVerificationCodeException() {
		super();
	}

	public InvalidVerificationCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidVerificationCodeException(String message) {
		super(message);
	}

	public InvalidVerificationCodeException(Throwable cause) {
		super(cause);
	}
}
