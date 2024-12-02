package com.ssafy.enjoytrip.graph.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRankWithAgeDto {
	private int age;
	private String keyword;
	private int searchCount;
}
