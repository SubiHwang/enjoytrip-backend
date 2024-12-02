package com.ssafy.enjoytrip.follows.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.ssafy.enjoytrip.follows.model.FollowListResponseDto;
import com.ssafy.enjoytrip.follows.model.FollowRequestDto;
import com.ssafy.enjoytrip.follows.model.FollowResponseDto;
import com.ssafy.enjoytrip.follows.model.service.FollowService;
import com.ssafy.enjoytrip.members.model.service.MemberService;
import com.ssafy.enjoytrip.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/follows")
@Tag(name = "팔로우 컨트롤러", description = "팔로우 조회, 추가, 삭제 등 팔로우정보를 관리하는 컨트롤러")
public class FollowController {

	private final JWTUtil jwtUtil;

	private final FollowService followService;

	// private final MemberService memberService;

	public FollowController(JWTUtil jwtUtil, FollowService followService) {
		this.jwtUtil = jwtUtil;
		this.followService = followService;
		// this.memberService = memberService;
	}

	@Operation(summary = "팔로우/팔로잉 조회", description = "팔로우/팔로잉 목록을 조회합니다.")
	@GetMapping("/{userId}/info")
	public ResponseEntity<?> getFollowInfo(@RequestHeader("Authorization") String authHeader,
			@PathVariable String userId) {

		try {
			// 토큰에서 사용자 ID 추출
			String token = authHeader.substring(7);
			String tokenId = jwtUtil.getMemberIdFromToken(token);

			System.out.println(userId);
			System.out.println(tokenId);

			// 이러면 팔로잉 여부 확인 가능!
			FollowResponseDto response = followService.getFollowInfo(userId, tokenId);

			log.info("아이디: {}", response.getUserId());
			log.info("프로필 URL 조회: {}", response.getProfileUrl());
			log.info("팔로워 수: {}", response.getFollowerCnt());
			log.info("팔로잉 수: {}", response.getFollowingCnt());
			log.info("팔로우 여부: {}", response.isFollowing());

			return ResponseEntity.ok().contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
					.body(response);
		} catch (Exception e) {
			log.error("친구 정보 조회 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 정보 조회 실패");
		}
	}

	@Operation(summary = "팔로우/팔로잉 목록 리스트 조회", description = "팔로우/팔로잉 목록 리스트 조회합니다.")
	@GetMapping("/{userId}/infoList")
	public ResponseEntity<?> getFollowListInfo(@RequestHeader("Authorization") String authHeader,
			@PathVariable String userId) {
		try {
			// 토큰에서 사용자 ID 추출
			String token = authHeader.substring(7);
			String tokenId = jwtUtil.getMemberIdFromToken(token);
			
			System.out.println(userId);

			// tokenId가 null이면 인증 실패로 처리
			if (tokenId == null) {
				System.out.println("토큰 null");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
			}
			
			// 구체적인 에러 로깅 추가
            log.info("Requesting follow lists for userId: {}", userId);

			// 팔로워와 팔로잉 목록을 모두 포함하는 응답 DTO
			Map<String, List<FollowListResponseDto>> response = followService.getAllFollowLists(userId);

			return ResponseEntity.ok().contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
					.body(response);

		} catch (Exception e) {
			log.error("친구 정보 리스트 조회 실패: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 정보 리스트 조회 실패");
		}
	}

	@Operation(summary = "친구 추가", description = "친구를 추가합니다.")
	@PostMapping
	public ResponseEntity<?> addFollow(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@RequestBody FollowRequestDto requestDto) throws Exception {

		// 토큰에서 사용자 ID 추출
		String token = authHeader.substring(7);
		String userId = jwtUtil.getMemberIdFromToken(token);

		System.out.println(userId);

		// 로그 추가
		log.debug("팔로우 요청 - followerId: {}, followingId: {}", userId, requestDto.getFollowingId());

		try {
			int result = followService.addFollow(userId, requestDto);

			if (result >= 1) {

				return ResponseEntity.ok().contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
						.body(Map.of("message", "친구 추가가 완료되었습니다.", "isFollowing", true // 성공하면 true로
						));
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
						.body(Map.of("message", "이미 팔로우한 사용자입니다.", "isFollowing", true // 여기도 isFollowing 추가
						));
			}

		} catch (Exception e) {
			log.error("친구 추가 실패 : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
					.body(Map.of("message", "친구 추가 처리 중 오류가 발생했습니다."));
		}

	}

	@Operation(summary = "친구 삭제", description = "친구를 삭제합니다.")
	@DeleteMapping("/{followingId}") // PathVariable로 변경
	public ResponseEntity<?> deleteFollow(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
			@PathVariable String followingId) throws Exception {

		String token = authHeader.substring(7);
		String memberId = jwtUtil.getMemberIdFromToken(token);

		// 로그 추가
		log.debug("팔로잉 취소 - followingId: {}", followingId);

		try {
			int result = followService.deleteFollow(memberId, followingId); // 두 개의 ID 모두 전달
			if (result >= 1) {
				return ResponseEntity.ok().contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
						.body(Map.of("message", "친구 삭제가 완료되었습니다."));
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
						.body(Map.of("message", "친구 목록에 존재하지 않습니다."));
			}

		} catch (Exception e) {
			log.error("친구 삭제 실패 : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
					.body(Map.of("message", "친구 삭제 처리 중 오류가 발생했습니다."));
		}
	}

}
