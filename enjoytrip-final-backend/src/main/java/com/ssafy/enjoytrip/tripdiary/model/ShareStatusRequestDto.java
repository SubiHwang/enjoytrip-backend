package com.ssafy.enjoytrip.tripdiary.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Schema(description = "여행일기 공유상태 변경 정보")
public class ShareStatusRequestDto {
	@Schema(description = "여행 일정 루트 정보", example = "true")
	private Boolean share;
}
