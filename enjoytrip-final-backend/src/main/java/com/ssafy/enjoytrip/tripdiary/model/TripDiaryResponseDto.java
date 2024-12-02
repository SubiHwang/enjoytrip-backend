package com.ssafy.enjoytrip.tripdiary.model;

import java.util.List;

import com.ssafy.enjoytrip.members.model.MemberResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDiaryResponseDto {
	private int diaryId;
	private MemberResponseDto member;
	private String title;
	private String content;
	private String thumbnailUrl;
	private String startDate;
	private String endDate;
	private boolean hasMap;
	private boolean isPublic;
	private boolean isLike;
	private String createdAt;
	private int totalDays;
	private boolean isOwned;
	private List<TripRouteResponseDto> tripRoutes;
}
