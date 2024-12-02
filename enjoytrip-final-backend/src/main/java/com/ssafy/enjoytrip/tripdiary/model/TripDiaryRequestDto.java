package com.ssafy.enjoytrip.tripdiary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "여행 일기 정보")
public class TripDiaryRequestDto {

   @Schema(description = "여행 제목", example = "제주도 3박 4일 여행")
   private String title;

   @Schema(description = "여행 내용", example = "첫째날은 성산일출봉...")
   private String content;
   
   @Schema(description = "대표 사진", example = "https://cataas.com/cat")
   private String thumbnailUrl;
   
   @Schema(description = "여행 시작일", example = "2024-03-20")
   private String startDate;
   
   @Schema(description = "여행 종료일", example = "2024-03-20") 
   private String endDate;
   
   @Schema(description = "지도 사용 여부", example = "true")
   private Boolean useMap;
   
   @Schema(description = "방문 장소 목록")
   private List<TripRouteRequestDto> places;
   
   @Schema(description = "총 여행 일수", example = "1")
   private Integer totalDays;
}