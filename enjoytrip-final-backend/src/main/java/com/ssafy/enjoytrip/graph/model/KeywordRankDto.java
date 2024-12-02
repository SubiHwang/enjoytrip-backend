package com.ssafy.enjoytrip.graph.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRankDto {
	private String keyword;
	private int searchCount;
}
