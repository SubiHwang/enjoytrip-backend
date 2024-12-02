package com.ssafy.enjoytrip.follows.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto {

	private int followId;
    private String followerId;
    private String followingId;
    private String createdAt;
	
}
