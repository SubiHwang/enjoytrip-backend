package com.ssafy.enjoytrip.tripdiary.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.tripdiary.model.ShareStatusRequestDto;
import com.ssafy.enjoytrip.tripdiary.model.TripDiaryRequestDto;
import com.ssafy.enjoytrip.tripdiary.model.service.TripDiaryService;
import com.ssafy.enjoytrip.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/trip-diary")
@Tag(name = "여행일기 컨트롤러", description = "여행일기 조회, 추가 및 수정, 삭제 등 여행일기 정보를 관리하는 컨트롤러")
public class TripDiaryController {

	private final TripDiaryService tripDiaryService;
	private final JWTUtil jwtUtil;

	public TripDiaryController(TripDiaryService tripDiaryService, JWTUtil jwtUtil) {
		super();
		this.tripDiaryService = tripDiaryService;
		this.jwtUtil = jwtUtil;
	}
	
	@Operation(summary = "여행일기 전체 목록 조회", description = "여행일기 전체 목록 조회합니다.")
	@GetMapping("/all")
	public ResponseEntity<?> getAllTripDiaries(
			@Parameter(hidden = true) @RequestHeader(name = "Authorization", required = false, defaultValue = "") String authHeader) {
		log.info("여행일기 전체 목록 조회 요청");
		
		String memberId = null;
		if(!authHeader.equals("")) {
			String token = authHeader.substring(7);
			memberId = jwtUtil.getMemberIdFromToken(token);
		}
		
		try {
			return ResponseEntity.ok().body(tripDiaryService.getAllTripDiaries(memberId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("여행일기 전체 목록 조회 실패");
		}
	}
	
	@Operation(summary = "여행일기 팔로잉 목록 조회", description = "여행일기 팔로잉 목록 조회합니다.")
	@GetMapping("/following")
	public ResponseEntity<?> getFollowingTripDiaries(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
		log.info("여행일기 팔로잉 목록 조회 요청");
		
		String token = authHeader.substring(7);
		String memberId = jwtUtil.getMemberIdFromToken(token);
		
		try {
			return ResponseEntity.ok().body(tripDiaryService.getFollowingTripDiaries(memberId));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("여행일기 팔로잉 목록 조회 실패");
		}
	}
	
	@Operation(summary = "나의 여행일기 목록 조회", description = "나의 여행일기 목록 조회합니다.")
	@GetMapping("/account/{userId}")
	public ResponseEntity<?> getAccountTripDiaries(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable("userId") String userId) {
		log.info("나의 여행일기 목록 조회 요청");
		
		String token = authHeader.substring(7);
		String loginId = jwtUtil.getMemberIdFromToken(token);
		
		try {
			return ResponseEntity.ok().body(tripDiaryService.getAccountTripDiaries(userId, loginId));
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("나의 여행일기 목록 조회 실패");
		}
	}
	
	@Operation(summary = "여행일기 추가", description = "여행일기를 추가합니다.")
	@PostMapping("/")
	public ResponseEntity<?> createTripDiary(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@RequestBody TripDiaryRequestDto tripDiaryRequestDto) {
		log.info("여행일기 추가 요청 : {}", tripDiaryRequestDto);
		
		String token = authHeader.substring(7);
		String memberId = jwtUtil.getMemberIdFromToken(token);
		
		return ResponseEntity.ok().body(tripDiaryService.createTripDiary(memberId, tripDiaryRequestDto));			
	}
	
	@Operation(summary = "여행일기 조회", description = "여행일기를 조회합니다.")
	@GetMapping("/{diaryId}")
	public ResponseEntity<?> getTripDiary(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable Integer diaryId) {
		log.info("여행일기 조회 요청 : ID: {}", diaryId);
		
		String token = authHeader.substring(7);
		String memberId = jwtUtil.getMemberIdFromToken(token);
		
		return ResponseEntity.ok().body(tripDiaryService.getTripDiary(memberId, diaryId));
	}
	
	@Operation(summary = "여행일기 수정", description = "여행일기를 수정합니다.")
	@PutMapping("/{diaryId}")
	public ResponseEntity<?> updateTripDiary(
	        @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
	        @PathVariable Integer diaryId,
	        @RequestBody TripDiaryRequestDto tripDiaryRequestDto) {
	    log.info("여행일기 수정 요청 : ID: {}, Data: {}", diaryId, tripDiaryRequestDto);

	    String token = authHeader.substring(7);
	    String memberId = jwtUtil.getMemberIdFromToken(token);
	    
	    tripDiaryService.updateTripDiary(memberId, diaryId, tripDiaryRequestDto);
	    return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "여행일기 삭제", description = "여행일기를 삭제합니다.")
	@DeleteMapping("/{diaryId}")
	public ResponseEntity<?> removeTripDiary(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable Integer diaryId) {
		log.info("여행일기 삭제 요청 : ID: {}", diaryId);
		
		String token = authHeader.substring(7);
	    String memberId = jwtUtil.getMemberIdFromToken(token);
	    
	    tripDiaryService.deleteTripDiary(memberId, diaryId);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "여행일기 공유상태 변경", description = "여행일기 공유상태를 변경합니다.")
	@PatchMapping("/{diaryId}/share-status")
	public ResponseEntity<?> updateShareStatusTripDiary (
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable Integer diaryId,
			@RequestBody ShareStatusRequestDto shareStatusRequestDto) {
		log.info("여행일기 공유상태 요청 : ID: {}, 변경요청: {}", diaryId, shareStatusRequestDto);
		
		String token = authHeader.substring(7);
	    String memberId = jwtUtil.getMemberIdFromToken(token);
	    
	    tripDiaryService.updateShareStatusTripDiary(memberId, diaryId, shareStatusRequestDto.getShare());
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "여행일기 좋아요", description = "여행일기에 좋아요를 추가합니다.")
	@PostMapping("/{diaryId}/like")
	public ResponseEntity<?> likeTripDiary (
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable Integer diaryId) {
		log.info("여행일기 좋아요 요청 : ID: {}", diaryId);
		
		String token = authHeader.substring(7);
	    String memberId = jwtUtil.getMemberIdFromToken(token);
	    
	    tripDiaryService.likeTripDiary(memberId, diaryId);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "여행일기 좋아요 취소", description = "여행일기에 좋아요를 취소합니다.")
	@DeleteMapping("/{diaryId}/like")
	public ResponseEntity<?> unlikeTripDiary (
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable Integer diaryId) {
		log.info("여행일기 좋아요 취소 요청 : ID: {}", diaryId);
		
		String token = authHeader.substring(7);
	    String memberId = jwtUtil.getMemberIdFromToken(token);
	    
	    tripDiaryService.unlikeTripDiary(memberId, diaryId);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "여행일기 접근 권한 확인", description = "여행일기 접근 권환을 확인합니다.")
	@GetMapping("/{diaryId}/auth")
	public ResponseEntity<?> checkAuthrization (
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable Integer diaryId) {
		log.info("여행일기 접근 권한 확인 요청 : ID: {}", diaryId);
		
		String token = authHeader.substring(7);
	    String memberId = jwtUtil.getMemberIdFromToken(token);
	    
	    tripDiaryService.checkAuthrization(memberId, diaryId);
		return ResponseEntity.ok().build();
	}
}
