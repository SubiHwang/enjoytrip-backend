package com.ssafy.enjoytrip.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private final String message;
	private final String errorCode;

	public ErrorResponse(String message, String errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}
}
