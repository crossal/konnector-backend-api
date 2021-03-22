package com.konnector.backendapi.http;

import com.konnector.backendapi.exceptions.EmailVerificationSendException;
import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.exceptions.InvalidLoginDetailsException;
import com.konnector.backendapi.exceptions.InvalidVerificationCodeException;
import com.konnector.backendapi.exceptions.NoVerificationAttemptsLeftException;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.exceptions.UnauthorizedException;
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

	@ExceptionHandler(UnauthorizedException.class)
	protected ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e){
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponse.Builder().withError("Not allowed").build());
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse.Builder().withError("Requested resource not found").build());
	}

	@ExceptionHandler(InvalidDataException.class)
	protected ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException e){
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(NoVerificationAttemptsLeftException.class)
	protected ResponseEntity<ErrorResponse> handleNoVerificationAttemptsLeftException(NoVerificationAttemptsLeftException e){
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(InvalidVerificationCodeException.class)
	protected ResponseEntity<ErrorResponse> handleInvalidVerificationCodeException(InvalidVerificationCodeException e){
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(InvalidLoginDetailsException.class)
	protected ResponseEntity<ErrorResponse> handleInvalidLoginDetailsExceptionException(InvalidLoginDetailsException e){
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(EmailVerificationSendException.class)
	protected ResponseEntity<ErrorResponse> handleEmailVerificationSendException(EmailVerificationSendException e){
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse.Builder().withError(e.getMessage()).build());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> exception(Exception e) {

		logger.error(e);

		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse.Builder().withError("Internal server error").build());
	}
}
