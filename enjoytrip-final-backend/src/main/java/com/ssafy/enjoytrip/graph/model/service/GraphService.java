package com.ssafy.enjoytrip.graph.model.service;

import java.util.List;

import com.ssafy.enjoytrip.graph.model.KeywordRankDto;
import com.ssafy.enjoytrip.graph.model.KeywordRankWithAgeDto;
import com.ssafy.enjoytrip.graph.model.KeywordRanksByAllAgeResponseDto;

public interface GraphService {

	int saveKeywordHistory(String userId, String keyword);

	List<KeywordRankDto> getKeywordSearchRanksByAll();

	KeywordRanksByAllAgeResponseDto getKeywordSearchRanksByUser(String userId);
	
	List<KeywordRanksByAllAgeResponseDto> getKeywordSearchRanksByAge();

}
