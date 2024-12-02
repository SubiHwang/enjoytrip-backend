package com.ssafy.enjoytrip.trip.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
	@Schema(description = "시도 코드", example = "1", required = false)
    private Integer sidoCode;
	
	@Schema(description = "구군 코드", example = "12", required = false)
    private Integer gugunCode;
	
	@Schema(description = "카테고리 코드 (12: 관광지, 14: 문화시설, 15: 축제공연행사, 25: 여행코스, 28: 레포츠, 32: 숙박, 38: 쇼핑, 39: 음식점)", 
	           example = "12", 
	           required = false)
    private Integer category;
	
	@Schema(description = "검색 키워드", example = "공원", required = false)
    private String keyword;
}
