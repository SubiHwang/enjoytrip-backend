package com.ssafy.enjoytrip.follows.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowResponseDto {
    private String userId;        // 사용자 ID
    private String profileUrl;    // 프로필 이미지 URL
    private int followingCnt; 
    private int followerCnt;
    
    @JsonProperty("isFollowing")  // JSON 변환 시 이름 유지하도록!
    private boolean isFollowing;  // 현재 사용자가 이 사용자를 팔로우하고 있는지 여부

	@Override
	public String toString() {
		return "FollowResponseDto [userId=" + userId + ", profileUrl=" + profileUrl + ", followingCnt=" + followingCnt
				+ ", followerCnt=" + followerCnt + ", isFollowing=" + isFollowing + "]";
	}
    
    
    
}
