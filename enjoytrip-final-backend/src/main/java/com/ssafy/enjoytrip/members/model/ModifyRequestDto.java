package com.ssafy.enjoytrip.members.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyRequestDto {
	@Schema(description = "회원 ID", example = "ssafy")
	private String userId; // 회원 ID

	@Schema(description = "회원 이름", example = "싸피킴")
	private String userName; // 회원 이름

	@Schema(description = "비밀번호", example = "1234")
	private String userPassword; // 비밀번호

	@Schema(description = "이메일 ID", example = "ssafy")
	private String emailId; // 이메일 ID

	@Schema(description = "이메일 도메인", example = "ssafy.com")
	private String emailDomain; // 이메일 도메인

	@Schema(description = "성별 도메인", example = "0")
	private String gender; // 이메일 도메인

	@Schema(description = "생일 도메인", example = "1999-09-09")
	private String birth; // 이메일 도메인

	@Schema(description = "이미지 url 도메인", example = "/./")
	private String profileUrl; // 이메일 도메인

}
