package com.ssafy.enjoytrip.graph.model;

import java.util.List;

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
public class KeywordRanksByAllAgeResponseDto {
	private int age;
	private List<KeywordRankDto> keywords;
}
