package com.konnector.backendapi.http;

import com.konnector.backendapi.exceptions.EmailVerificationSendException;
import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.exceptions.InvalidLoginDetailsException;
import com.konnector.backendapi.exceptions.InvalidVerificationCodeException;
import com.konnector.backendapi.exceptions.NoVerificationAttemptsLeftException;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.exceptions.UnauthorizedException;
import com.konnector.backendapi.exceptions.UserNotVerifiedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(UserNotVerifiedException.class)
	protected ResponseEntity<ErrorResponse> handleException(UserNotVerifiedException e){
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).withCode(ErrorResponseCodes.USER_NOT_VERIFIED).build());
	}

	@ExceptionHandler(UnauthorizedException.class)
	protected ResponseEntity<ErrorResponse> handleException(UnauthorizedException e){
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponse.Builder().withError("Not allowed").build());
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleException(NotFoundException e){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse.Builder().withError("Requested resource not found").build());
	}

	@ExceptionHandler(InvalidDataException.class)
	protected ResponseEntity<ErrorResponse> handleException(InvalidDataException e){
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(NoVerificationAttemptsLeftException.class)
	protected ResponseEntity<ErrorResponse> handleException(NoVerificationAttemptsLeftException e){
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(InvalidVerificationCodeException.class)
	protected ResponseEntity<ErrorResponse> handleException(InvalidVerificationCodeException e){
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(InvalidLoginDetailsException.class)
	protected ResponseEntity<ErrorResponse> handleException(InvalidLoginDetailsException e){
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(EmailVerificationSendException.class)
	protected ResponseEntity<ErrorResponse> handleException(EmailVerificationSendException e){
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<ErrorResponse> handleException(BadCredentialsException e) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {

		logger.error(e);

		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse.Builder().withError("Internal server error").build());
	}
}
