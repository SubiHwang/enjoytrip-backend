package com.ssafy.enjoytrip.members.model;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원 정보 DTO")
public class MemberDto {

	private String userId;
	private String role;
	private String userName;
	private String userPassword;
	private String emailId;
	private String emailDomain;
	private Integer gender; // TINYINT(1)
	private String birth;
	private String profileUrl;
	private LocalDateTime joinDate;
	private String token;

	public String getFullEmail() {
		return this.emailId + "@" + this.emailDomain;
	}

	@Override
	public String toString() {
		return "MemberDto [userId=" + userId + ", role=" + role + ", userName=" + userName + ", userPassword="
				+ userPassword + ", emailId=" + emailId + ", emailDomain=" + emailDomain + ", gender=" + gender
				+ ", birth=" + birth + ", profileUrl=" + profileUrl + ", joinDate=" + joinDate + ", token=" + token
				+ "]";
	}

}
