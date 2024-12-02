package com.ssafy.enjoytrip.tripdiary.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDiaryDto {
	private int diaryId; // diary_id (INT)
	private String userId; // user_id (VARCHAR(16))
	private String title; // title (VARCHAR(100))
	private String content; // content (TEXT)
	private String thumbnailUrl; // VARCHAR(2028)
	private String startDate; // start_date (DATE)
	private String endDate; // end_date (DATE)
	private boolean hasMap; // has_map (TINYINT(1))
	private boolean isPublic; // is_public (TINYINT(1))
	private String createdAt; // created_at (TIMESTAMP)
	private int totalDays; // total_days (INT)
}
