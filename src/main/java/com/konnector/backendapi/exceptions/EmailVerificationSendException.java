package com.konnector.backendapi.exceptions;

@SuppressWarnings("serial")
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
