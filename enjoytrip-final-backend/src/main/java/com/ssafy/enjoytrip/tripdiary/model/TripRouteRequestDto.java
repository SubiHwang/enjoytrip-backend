package com.ssafy.enjoytrip.tripdiary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "여행 일정 루트 정보")
public class TripRouteRequestDto {
    
    @Schema(description = "관광지 번호", example = "3818")
    private Integer no;
    
    @Schema(description = "해당 관광지의 여행 일차", example = "1")
    private Integer day;
    
    @Schema(description = "해당 관광지의 방문 순서", example = "1")
    private Integer visitOrder;
    
    @Schema(description = "시작 시간 정보")
    private TimeDto startTime;
    
    @Schema(description = "종료 시간 정보")
    private TimeDto endTime;
}