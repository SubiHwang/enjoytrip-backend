package com.ssafy.enjoytrip.follows.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoytrip.follows.model.FollowListResponseDto;
import com.ssafy.enjoytrip.follows.model.FollowRequestDto;
import com.ssafy.enjoytrip.follows.model.FollowResponseDto;

@Mapper
public interface FollowMapper {


	int addFollow(@Param("userId") String userId, @Param("requestDto") FollowRequestDto requestDto);

	int deleteFollow(@Param("userId")String userId, String followingId);

	String getUserProfileUrl(String userId);

	int countFollowing(String userId);

	int countFollowers(String userId);

	int checkFollowing(String userId, String tokenId);

	List<FollowListResponseDto> findFollowersByUserId(String userId);

	List<FollowListResponseDto> findFollowingsByUserId(String userId);


}
