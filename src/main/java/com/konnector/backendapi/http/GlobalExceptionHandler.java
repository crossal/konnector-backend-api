package com.konnector.backendapi.http;

import com.konnector.backendapi.exceptions.InvalidDataException;
import com.konnector.backendapi.exceptions.InvalidVerificationCodeException;
import com.konnector.backendapi.exceptions.NoVerificationAttemptsLeftException;
import com.konnector.backendapi.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<String> handleNotFoundException(NotFoundException e){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body("Requested resource not found");
	}

	@ExceptionHandler(InvalidDataException.class)
	protected ResponseEntity<String> handleInvalidDataException(InvalidDataException e){
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(e.getMessage());
	}

	@ExceptionHandler(NoVerificationAttemptsLeftException.class)
	protected ResponseEntity<String> handleNoVerificationAttemptsLeftException(NoVerificationAttemptsLeftException e){
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(e.getMessage());
	}

	@ExceptionHandler(InvalidVerificationCodeException.class)
	protected ResponseEntity<String> handleInvalidVerificationCodeException(InvalidVerificationCodeException e){
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> exception(Exception e) {

		logger.error(e);

		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Internal server error");
	}
}
