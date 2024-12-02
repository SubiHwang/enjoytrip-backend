package com.ssafy.enjoytrip.tripdiary.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDiaryCardDto {
	private int diaryId;
	private String thumbnailUrl;
	private String title;
	private String startDate;
	private String endDate;
	private String profileUrl;
	private String userId;
	private boolean isPublic;
	private boolean isLike;
	private boolean isOwned;
	private int likeCount;
}
