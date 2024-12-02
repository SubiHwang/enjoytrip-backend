package com.ssafy.enjoytrip.follows.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowRequestDto {
    private String followingId;   // 팔로우 대상이 되는 사용자
}
