package com.ssafy.enjoytrip.members.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
	@Schema(description = "회원 ID", example = "ssafy")
	private String userId; // 회원 ID
	
	@Schema(description = "비밀번호", example = "1234")
	private String userPassword; // 비밀번호
}
