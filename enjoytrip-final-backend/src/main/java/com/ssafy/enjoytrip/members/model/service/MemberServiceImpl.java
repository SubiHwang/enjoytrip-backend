package com.ssafy.enjoytrip.members.model.service;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.members.model.JoinRequestDto;
import com.ssafy.enjoytrip.members.model.LoginRequestDto;
import com.ssafy.enjoytrip.members.model.MemberDto;
import com.ssafy.enjoytrip.members.model.MemberResponseDto;
import com.ssafy.enjoytrip.members.model.ModifyRequestDto;
import com.ssafy.enjoytrip.members.model.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{
	
	private final MemberMapper memberMapper;

	public MemberServiceImpl(MemberMapper memberMapper) {
		super();
		this.memberMapper = memberMapper;
	}

	@Override
	public int joinMember(JoinRequestDto joinDto) throws Exception {
		return memberMapper.joinMember(joinDto);
	}

	@Override
	public MemberDto loginMember(LoginRequestDto loginDto) throws Exception {
		return memberMapper.loginMember(loginDto);
	}

	@Override
	public int modifyMember(ModifyRequestDto modifyDto) {
		return memberMapper.modifyMember(modifyDto);
	}

	@Override
	public int deleteMember(String userId) {
		return memberMapper.deleteMember(userId);
	}

	@Override
	public int saveRefreshToken(String userId, String refreshToken) throws Exception {
		return memberMapper.saveRefreshToken(userId, refreshToken);
	}

	@Override
	public int deleteRefreshToken(String userId) throws Exception {
		return memberMapper.deleteRefreshToken(userId);
	}

	@Override
	public boolean isRefreshTokenValid(String userId, String token) throws Exception {
		MemberDto memberDto =  memberMapper.selectRefreshTokenByMember(userId, token);
		if(memberDto == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public MemberResponseDto getMyProfileInfo(String userId) {
		return memberMapper.getMyProfileInfo(userId);
	}

	@Override
	public int getMemberAge(String userId) {
		return memberMapper.getMemberAge(userId);
	}

}
