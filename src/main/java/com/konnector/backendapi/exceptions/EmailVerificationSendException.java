package com.konnector.backendapi.exceptions;

public class EmailVerificationSendException extends RuntimeException {

	public EmailVerificationSendException() {
		super();
	}

	public EmailVerificationSendException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmailVerificationSendException(String message) {
		super(message);
	}

	public EmailVerificationSendException(Throwable cause) {
		super(cause);
	}
}
