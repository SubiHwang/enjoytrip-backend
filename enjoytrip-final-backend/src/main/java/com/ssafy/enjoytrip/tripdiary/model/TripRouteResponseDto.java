package com.ssafy.enjoytrip.tripdiary.model;

import com.ssafy.enjoytrip.trip.model.TripDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRouteResponseDto {
	private int routeId;
    private int diaryId;
    private TripDto attraction;
    private String detailStartTime;
    private String detailEndTime;
    private int day;
    private int visitOrder;
    private String createdAt;
}
