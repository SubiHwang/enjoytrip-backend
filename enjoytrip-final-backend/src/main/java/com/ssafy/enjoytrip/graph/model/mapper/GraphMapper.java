package com.ssafy.enjoytrip.graph.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoytrip.graph.model.KeywordRankDto;
import com.ssafy.enjoytrip.graph.model.KeywordRankWithAgeDto;

@Mapper
public interface GraphMapper {
	
	int saveKeywordHistory(String userId, String keyword);
	
	List<KeywordRankDto> getKeywordSearchRanksByAll();
	
	List<KeywordRankWithAgeDto> getKeywordSearchRanksByAllAge();
	
}
