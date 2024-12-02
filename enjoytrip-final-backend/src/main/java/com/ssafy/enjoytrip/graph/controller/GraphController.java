package com.ssafy.enjoytrip.graph.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.graph.model.KeywordRequestDto;
import com.ssafy.enjoytrip.graph.model.service.GraphService;
import com.ssafy.enjoytrip.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/graph")
@Tag(name = "통계 컨트롤러", description = "서비스 관련 통계를 관리하는 컨트롤러")
public class GraphController {

	private final JWTUtil jwtUtil;
	
	private final GraphService graphService;
	
	public GraphController(JWTUtil jwtUtil, GraphService graphService) {
		super();
		this.jwtUtil = jwtUtil;
		this.graphService = graphService;
	}



	@Operation(summary = "키워드 저장", description = "키워드를 저장합니다.")
	@PostMapping("/keyword")
	public ResponseEntity<?> saveKeywordHistory(
			@Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false, defaultValue = "") String authHeader,
			@RequestBody KeywordRequestDto keywordRequest) {
		
		String memberId = null;
		if(!authHeader.equals("")) {
			String token = authHeader.substring(7);
			memberId = jwtUtil.getMemberIdFromToken(token);
		}

		graphService.saveKeywordHistory(memberId, keywordRequest.getKeyword());
		
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "전체 Top5 키워드를 제공", description = "전체 Top5 키워드를 제공합니다.")
	@GetMapping("/keyword/all")
	public ResponseEntity<?> getKeywordSearchRanks() {
		
		return ResponseEntity.ok().body(graphService.getKeywordSearchRanksByAll());
	}
	
	@Operation(summary = "사용자 연령대의 Top3 키워드를 제공", description = "사용자 연령대의 Top3 키워드를 제공합니다.")
	@GetMapping("/keyword/user")
	public ResponseEntity<?> getKeywordSearchRanksByUser(
			@Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false, defaultValue = "") String authHeader) {
		
		String token = authHeader.substring(7);
		String memberId = jwtUtil.getMemberIdFromToken(token);
		
		return ResponseEntity.ok().body(graphService.getKeywordSearchRanksByUser(memberId));
	}
	
	@Operation(summary = "모든 연령대의 Top3 키워드를 제공", description = "사용자 연령대의 Top3 키워드를 제공합니다.")
	@GetMapping("/keyword/age")
	public ResponseEntity<?> getKeywordSearchRanksByAll() {

		return ResponseEntity.ok().body(graphService.getKeywordSearchRanksByAge());
	}
}
