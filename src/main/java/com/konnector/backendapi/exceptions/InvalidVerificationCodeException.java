package com.konnector.backendapi.exceptions;

@SuppressWarnings("serial")
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
