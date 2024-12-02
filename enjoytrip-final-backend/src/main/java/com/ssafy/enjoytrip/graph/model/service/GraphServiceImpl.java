package com.ssafy.enjoytrip.graph.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.exception.CustomException;
import com.ssafy.enjoytrip.exception.ErrorCode;
import com.ssafy.enjoytrip.graph.model.KeywordRankDto;
import com.ssafy.enjoytrip.graph.model.KeywordRankWithAgeDto;
import com.ssafy.enjoytrip.graph.model.KeywordRanksByAllAgeResponseDto;
import com.ssafy.enjoytrip.graph.model.mapper.GraphMapper;
import com.ssafy.enjoytrip.members.model.mapper.MemberMapper;

@Service
public class GraphServiceImpl implements GraphService {

	private final GraphMapper graphMapper;
	private final MemberMapper memberMapper;

	public GraphServiceImpl(GraphMapper graphMapper, MemberMapper memberMapper) {
		super();
		this.graphMapper = graphMapper;
		this.memberMapper = memberMapper;
	}

	@Override
	public int saveKeywordHistory(String userId, String keyword) {
		int result = 0;
		try {
			result = graphMapper.saveKeywordHistory(userId, keyword);
			if (result == 0) {
				throw new CustomException(ErrorCode.SERVER_ERROR);
			}
		} catch (Exception e) {
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}
		return result;
	}

	@Override
	public List<KeywordRankDto> getKeywordSearchRanksByAll() {
		return graphMapper.getKeywordSearchRanksByAll();
	}

	@Override
	public KeywordRanksByAllAgeResponseDto getKeywordSearchRanksByUser(String userId) {
		List<KeywordRankWithAgeDto> keywords = graphMapper.getKeywordSearchRanksByAllAge();
		int userAge = memberMapper.getMemberAge(userId);
		
		KeywordRanksByAllAgeResponseDto result = new KeywordRanksByAllAgeResponseDto();
		result.setAge(userAge);
		result.setKeywords(new ArrayList<>());
		
		for (KeywordRankWithAgeDto keyword : keywords) {
			if(keyword.getAge() == userAge) {
				result.getKeywords().add(new KeywordRankDto(keyword.getKeyword(), keyword.getSearchCount()));
			}
		}
		return result;
	}
	
	@Override
	public List<KeywordRanksByAllAgeResponseDto> getKeywordSearchRanksByAge() {
	    List<KeywordRanksByAllAgeResponseDto> result = new ArrayList<>();
	    
	    // 1. 먼저 모든 키워드 데이터를 가져옵니다
	    List<KeywordRankWithAgeDto> keywords = graphMapper.getKeywordSearchRanksByAllAge();
	    
	    // 2. age를 기준으로 그룹화합니다
	    Map<Integer, List<KeywordRankWithAgeDto>> groupedByAge = keywords.stream()
	        .collect(Collectors.groupingBy(KeywordRankWithAgeDto::getAge));
	    
	    // 3. 각 연령대별로 KeywordRankDto 리스트를 생성합니다
	    for (Map.Entry<Integer, List<KeywordRankWithAgeDto>> entry : groupedByAge.entrySet()) {
	        int age = entry.getKey();
	        List<KeywordRankWithAgeDto> ageKeywords = entry.getValue();
	        
	        // 해당 연령대의 키워드들을 KeywordRankDto로 변환
	        List<KeywordRankDto> keywordRanks = ageKeywords.stream()
	            .map(k -> new KeywordRankDto(k.getKeyword(), k.getSearchCount()))
	            .collect(Collectors.toList());
	        
	        // 결과 DTO를 생성하여 리스트에 추가
	        result.add(new KeywordRanksByAllAgeResponseDto(age, keywordRanks));
	    }
	    
	    // 4. 연령대 순으로 정렬
	    result.sort((a, b) -> Integer.compare(a.getAge(), b.getAge()));
	    
	    return result;
	}

}
