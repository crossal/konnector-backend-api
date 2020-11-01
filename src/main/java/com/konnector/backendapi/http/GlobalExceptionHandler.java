package com.konnector.backendapi.http;

import com.konnector.backendapi.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<String> handleNotFoundException(NotFoundException e){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body("Requested resource not found");
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> exception(Exception e) {
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Internal server error");
	}
}
