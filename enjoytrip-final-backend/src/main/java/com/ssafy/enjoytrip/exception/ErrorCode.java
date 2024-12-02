package com.ssafy.enjoytrip.exception;

public enum ErrorCode {
	// 인증 관련 에러 (401)
	TOKEN_EXPIRED("001", "토큰이 만료되었습니다."),
	TOKEN_NOT_FOUND("002", "토큰이 없습니다."),

	// 권한 없음 (403)
	FORBIDDEN("001", "권한이 없습니다."),
	// 리소스 없음 (404)
	NOT_FOUND("001", "리소스를 찾을 수 없습니다."),
	// 잘못된 요청 (400)
	BAD_REQUEST("001", "잘못된 요청입니다."),
	EMPTY_FILE("002", "파일이 비어있습니다."),
	NOT_IMAGE_FILE("003", "이미지 파일만 업로드 가능합니다."),
	OVER_MAX_SIZE("004", "파일 크기는 10MB를 초과할 수 없습니다."),
	// 리소스 충돌 (409)
	CONFLICT("001", "리소스가 충돌되었습니다."),
	DUPLICATE_LIKE("002", "중복된 좋아요 요청입니다."),
	// 서버 에러 (500)
	SERVER_ERROR("001", "서버 에러가 발생했습니다.");

	private String code;
	private String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
