package com.ssafy.enjoytrip.tripdiary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TimeDto {
	 @Schema(description = "시", example = "02")
	private String hour;
	 
	 @Schema(description = "분", example = "30")
	private String minute;
	 
	 @Schema(description = "오전/오후", example = "PM")
	private String period;
}
