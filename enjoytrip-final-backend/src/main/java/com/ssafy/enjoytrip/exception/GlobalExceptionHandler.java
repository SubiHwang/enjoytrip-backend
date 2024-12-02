package com.ssafy.enjoytrip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
		ErrorResponse response = new ErrorResponse(ex.getErrorCode().getMessage(), ex.getErrorCode().getCode());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // 또는 다른 적절한 상태코드
	}
}