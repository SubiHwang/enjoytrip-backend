package com.ssafy.enjoytrip.members.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {

	@Schema(description = "사용자 ID", example = "ssafy")
	private String userId;

	@Schema(description = "사용자 이름", example = "김싸피")
	private String userName;

	@Schema(description = "이메일 ID", example = "ssafy")
	private String emailId;

	@Schema(description = "이메일 도메인", example = "ssafy.com")
	private String emailDomain;

	@Schema(description = "성별", example = "1", allowableValues = { "0", "1", "2" })
	private String gender; // 0: 선택안함, 1: 남성, 2: 여성

	@Schema(description = "생년월일", example = "1999-09-09")
	private String birth;

	@Schema(description = "프로필 이미지 URL", example = "https://kr.object.ncloudstorage.com/enjoy-trip/profile-image.png")
	private String profileUrl;

}
