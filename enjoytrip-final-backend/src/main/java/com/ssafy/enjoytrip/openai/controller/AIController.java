package com.ssafy.enjoytrip.openai.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.enjoytrip.graph.model.KeywordRankDto;
import com.ssafy.enjoytrip.graph.model.KeywordRanksByAllAgeResponseDto;
import com.ssafy.enjoytrip.graph.model.service.GraphService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ai")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AIController {

	private final ChatClient chatClient;
	private final GraphService graphService;

	@Operation(summary = "관광지 추천 서비스", description = "연령대에 맞는 관광지 추천 서비스를 제공합니다.")
	@GetMapping("/chat")
	public ResponseEntity<?> getTourRecommendations(
			@RequestParam(required = false, defaultValue = "20") String age) {
		try {
			
			// 연령대별 인기 관광지 데이터 가져오기
            List<KeywordRanksByAllAgeResponseDto> ageRankings = graphService.getKeywordSearchRanksByAge();
            String topLocations = getTopLocationsForAge(ageRankings, age);
        
			// 프롬프트 텍스트 생성
			String promptText = String.format("""
					당신은 전문 여행 컨설턴트입니다.
					다음 형식으로 추천 관광지 3곳을 응답해주세요:

					1. [관광지명]
					- 관광지 위치:
					- 특징:
					- 추천 포인트:
					- 꿀팁:

					2. [관광지명]
					- 관광지 위치:
					- 특징:
					- 추천 포인트:
					- 꿀팁:

					3. [관광지명]
					- 관광지 위치:
					- 특징:
					- 추천 포인트:
					- 꿀팁:

					응답은 반드시 한글로 해주세요.
					각 장소마다 %s대의 취향을 고려한 구체적인 설명을 포함해주세요.

					사용자: 나는 %s대인데, 이 웹 사이트를 통해서 여행 루트도 짜고 여행 기록도 남기려고 해.
					이 웹 사이트에는 카카오 맵을 이용해서 관광지를 시도별로 검색해서 가고 싶은 여행지가 있으면 지도에서 클릭 후 내 여행 경로로 등록할 수 있어.
					여행을 너무 가고 싶지만, 어떤 곳을 가야 할지 모르겠어.
					내 취향에 맞는 관광지 3곳을 추천해줘.
					""", age, age);

			// Prompt 생성 및 API 호출
			Prompt prompt = new Prompt(promptText);
			ChatResponse response = chatClient.call(prompt);

			log.info("Generated recommendations for age: {}", age);

			// ChatResponse에서 텍스트 내용만 추출
			return ResponseEntity.ok(response.getResult().getOutput().getContent());

		} catch (Exception e) {
			
			// OpenAI API 할당량 초과 에러 체크
	        if (e.getMessage().contains("429") || 
	            e.getMessage().contains("insufficient_quota")) {
	            return ResponseEntity
	                .status(HttpStatus.TOO_MANY_REQUESTS)
	                .body("현재 서비스 사용량이 많아 잠시 후에 다시 시도해주세요.");
	        }
	        
			log.error("Error generating tour recommendations", e);
			return ResponseEntity.internalServerError().body("추천 생성 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
	
	// 연령대에 맞는 top3 관광지 정보 추출
	private String getTopLocationsForAge(List<KeywordRanksByAllAgeResponseDto> ageRankings, String age) {
	    StringBuilder sb = new StringBuilder();
	    int ageNum = Integer.parseInt(age);
	    
	    // 해당 연령대의 데이터 찾기
	    for (KeywordRanksByAllAgeResponseDto ranking : ageRankings) {
	        if (ranking.getAge() == ageNum) {
	            List<KeywordRankDto> keywords = ranking.getKeywords();
	            for (int i = 0; i < keywords.size(); i++) {
	                KeywordRankDto keywordRank = keywords.get(i);
	                sb.append(String.format("%d. %s (검색횟수: %d회)\n", 
	                    i + 1, 
	                    keywordRank.getKeyword(), 
	                    keywordRank.getSearchCount()));
	            }
	            break;
	        }
	    }
	    
	    if (sb.length() == 0) {
	        return "해당 연령대의 데이터가 없습니다.";
	    }
	    
	    return sb.toString();
	}
	
}