package com.ssafy.enjoytrip.members.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoytrip.members.model.JoinRequestDto;
import com.ssafy.enjoytrip.members.model.LoginRequestDto;
import com.ssafy.enjoytrip.members.model.MemberDto;
import com.ssafy.enjoytrip.members.model.MemberResponseDto;
import com.ssafy.enjoytrip.members.model.ModifyRequestDto;


@Mapper
public interface MemberMapper {

	int joinMember(JoinRequestDto joinDto);

	MemberDto loginMember(LoginRequestDto loginDto);

	int modifyMember(ModifyRequestDto modifyDto);

	int deleteMember(String userId);
	
	int saveRefreshToken(String userId, String refreshToken);
	
	int deleteRefreshToken(String userId);
	
	MemberDto selectRefreshTokenByMember(String userId, String refreshToken);

	MemberResponseDto getMyProfileInfo(String userId);

	int getMemberAge(String userId);
}
