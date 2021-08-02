package com.konnector.backendapi.exceptions;

public class UserNotVerifiedException extends RuntimeException {

	public UserNotVerifiedException() {
		super();
	}

	public UserNotVerifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotVerifiedException(String message) {
		super(message);
	}

	public UserNotVerifiedException(Throwable cause) {
		super(cause);
	}
}
