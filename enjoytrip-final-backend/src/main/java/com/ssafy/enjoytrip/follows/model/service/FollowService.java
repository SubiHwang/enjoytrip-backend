package com.ssafy.enjoytrip.follows.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.enjoytrip.follows.model.FollowListResponseDto;
import com.ssafy.enjoytrip.follows.model.FollowRequestDto;
import com.ssafy.enjoytrip.follows.model.FollowResponseDto;

public interface FollowService {

    
    int addFollow(String userId, FollowRequestDto requestDto);
    int deleteFollow(String userId, String followingId);
    
	FollowResponseDto getFollowInfo(String userId, String tokenId);
	
	
	Map<String, List<FollowListResponseDto>> getAllFollowLists(String userId);
    

	
}
