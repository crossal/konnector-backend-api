package com.konnector.backendapi.exceptions;

public class NoVerificationAttemptsLeftException extends RuntimeException  {

	public NoVerificationAttemptsLeftException() {
		super();
	}

	public NoVerificationAttemptsLeftException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoVerificationAttemptsLeftException(String message) {
		super(message);
	}

	public NoVerificationAttemptsLeftException(Throwable cause) {
		super(cause);
	}
}
