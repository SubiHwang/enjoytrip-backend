package com.ssafy.enjoytrip.members.model.service;

import com.ssafy.enjoytrip.members.model.JoinRequestDto;
import com.ssafy.enjoytrip.members.model.LoginRequestDto;
import com.ssafy.enjoytrip.members.model.MemberDto;
import com.ssafy.enjoytrip.members.model.MemberResponseDto;
import com.ssafy.enjoytrip.members.model.ModifyRequestDto;

public interface MemberService {

	int joinMember(JoinRequestDto joinDto) throws Exception;

	MemberDto loginMember(LoginRequestDto loginDto) throws Exception;

	int modifyMember(ModifyRequestDto modifyDto) throws Exception;

	int deleteMember(String userId) throws Exception;

	int saveRefreshToken(String userId, String refreshToken) throws Exception;

	int deleteRefreshToken(String userId) throws Exception;

	boolean isRefreshTokenValid(String userId, String token) throws Exception;

	MemberResponseDto getMyProfileInfo(String userId);

	int getMemberAge(String userId);

}
