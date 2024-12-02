package com.ssafy.enjoytrip.tripdiary.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRouteDto {
    private int routeId;        // route_id (INT)
    private int diaryId;        // diary_id (INT)
    private int attractionsNo;   // attractions_no (INT)
    private String detailStartTime;  // detail_start_time (String)
    private String detailEndTime;    // detail_end_time (String)
    private int day;            // day (INT)
    private int visitOrder;     // visit_order (INT)
    private String createdAt;  // created_at (String)
}
