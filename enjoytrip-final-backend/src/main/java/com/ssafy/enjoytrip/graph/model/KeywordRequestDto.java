package com.ssafy.enjoytrip.graph.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRequestDto {
	
	@Schema(description = "검색한 키워드", example = "해운대")
	private String keyword;
	
}
