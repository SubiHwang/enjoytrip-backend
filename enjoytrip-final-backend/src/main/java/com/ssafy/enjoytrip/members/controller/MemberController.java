package com.ssafy.enjoytrip.members.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ssafy.enjoytrip.follows.model.service.FollowService;
import com.ssafy.enjoytrip.members.model.JoinRequestDto;
import com.ssafy.enjoytrip.members.model.LoginRequestDto;
import com.ssafy.enjoytrip.members.model.MemberDto;
import com.ssafy.enjoytrip.members.model.MemberResponseDto;
import com.ssafy.enjoytrip.members.model.ModifyRequestDto;
import com.ssafy.enjoytrip.members.model.service.MemberService;
import com.ssafy.enjoytrip.service.S3Service;
import com.ssafy.enjoytrip.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/member")
@Tag(name = "회원관리 컨트롤러", description = "로그인, 회원가입, 사용자 인증토큰 발급 등 회원정보를 관리하는 컨트롤러")
public class MemberController {

	private final JWTUtil jwtUtil;

	private final MemberService memberService;

	private final FollowService followService;

	private final S3Service s3Service;

	public MemberController(MemberService memberService, JWTUtil jwtUtil, FollowService followService,
			S3Service s3Service) {
		super();
		this.memberService = memberService;
		this.jwtUtil = jwtUtil;
		this.followService = followService;
		this.s3Service = s3Service;
	}

	@Operation(summary = "로그인", description = "로그인을 처리합니다.")
	@PostMapping("/login")
	public ResponseEntity<?> loginMember(@RequestBody LoginRequestDto loginDto) throws Exception {
		MemberDto loginUser = memberService.loginMember(loginDto);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;

		if (loginUser != null) {
			// access, refresh token 발급
			String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
			String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
			log.debug("access token : {}", accessToken);
			log.debug("refresh tokne : {}", refreshToken);

			// refresh token을 DB에 저장
			memberService.saveRefreshToken(loginUser.getUserId(), refreshToken);

			// JSON으로 token 전달
			resultMap.put("access-token", accessToken);
			resultMap.put("refresh-token", refreshToken);
		

			// 사용자 정보도 함께 전달 (비밀번호는 제외)
			Map<String, Object> userInfo = new HashMap<>();
			userInfo.put("userId", loginUser.getUserId());

			System.out.println(loginUser.getProfileUrl());
			userInfo.put("profileUrl", loginUser.getProfileUrl());
			userInfo.put("role", loginUser.getRole());

			resultMap.put("userInfo", userInfo);

			status = HttpStatus.CREATED;
		} else {
			resultMap.put("message", "아이디 또는 패스워드를 확인해주세요.");
			status = HttpStatus.UNAUTHORIZED;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return ResponseEntity.status(status).headers(headers).body(resultMap);
	}

	@Operation(summary = "회원가입", description = "회원가입을 처리합니다.")
	@PostMapping("/")
	public ResponseEntity<?> joinMember(@RequestBody JoinRequestDto joinDto) throws Exception {
		log.info("회원가입 요청: {}", joinDto);

		if (joinDto.getUserId() == null || joinDto.getUserId().isEmpty()) {
			return ResponseEntity.badRequest().body("회원 ID는 필수 항목입니다.");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return ResponseEntity.ok().headers(headers).body(memberService.joinMember(joinDto));
	}

	@Operation(summary = "로그아웃", description = "로그아웃을 처리합니다.")
	@PostMapping(value = "/logout")
	public ResponseEntity<?> logoutMember(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
		HttpStatus status;
		Map<String, Object> resultMap = new HashMap<>();

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7); // "Bearer " 제거
			String memberId = jwtUtil.getMemberIdFromToken(token);

			try {
				memberService.deleteRefreshToken(memberId);
				resultMap.put("message", "로그아웃 성공");
				status = HttpStatus.OK;
			} catch (Exception e) {
				log.error("로그아웃 실패 : {}", e.getMessage(), e);
				resultMap.put("message", "로그아웃 처리 중 오류가 발생했습니다.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			resultMap.put("message", "유효하지 않은 토큰입니다.");
			status = HttpStatus.UNAUTHORIZED;
		}

		return ResponseEntity.status(status).body(resultMap);
	}

	@Operation(summary = "회원수정", description = "회원수정을 처리합니다.")
	@PutMapping("/")
	public ResponseEntity<?> modifyMember(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestPart("memberDto") ModifyRequestDto modifyDto) {
		try {
			// ""는 서버에서 보내는 이름!

			// 이미지 파일이 있으면 S3에 업로드
			if (file != null && !file.isEmpty()) {
				String imageUrl = s3Service.uploadImage(file);
				System.out.println(imageUrl);
				modifyDto.setProfileUrl(imageUrl);
			}

			int result = memberService.modifyMember(modifyDto);

			// S3에서 이미지 받아오기
			// String currentPath = request.getRequestURI();
//			    String path = currentPath.replace("/image/testSample","");

			// S3ObjectInputStream img = s3Service.getImage(currentPath);
			// S3ObjectInputStream형식을 IOUtils.toByteArray를 이용해 byte 데이터로 변환 후
			// ResponseEntity객체로 클라이언트에 전달

			if (result > 0) {
				// return
				// ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(IOUtils.toByteArray(img));
				return ResponseEntity.ok("회원 정보 수정 성공");

			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
			}
		} catch (Exception e) {
			log.error("회원 정보 수정 중 오류가 발생했습니다.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 수정 중 오류가 발생했습니다.");
		}
	}

	@Operation(summary = "회원탈퇴", description = "회원탈퇴를 처리합니다.")
	@DeleteMapping(value = "/")
	public ResponseEntity<?> deleteMember(@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			String memberId = jwtUtil.getMemberIdFromToken(token);

			try {
				int result = memberService.deleteMember(memberId);
				if (result > 0) {
					return ResponseEntity.ok("회원 탈퇴 성공");
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보를 찾을 수 없습니다.");
				}
			} catch (Exception e) {
				log.error("회원탈퇴 처리 중 오류가 발생했습니다: {}", e.getMessage(), e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원탈퇴 처리 중 오류가 발생했습니다.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
		}
	}

	@Operation(summary = "회원조회", description = "회원조회를 처리합니다.")
	@PostMapping("/myProfile")
	public ResponseEntity<?> getMyProfileInfo(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			String userId = jwtUtil.getMemberIdFromToken(token);

			System.out.println("회원조회");

			try {

				MemberResponseDto response = memberService.getMyProfileInfo(userId);

				return ResponseEntity.ok().contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
						.body(response);

			} catch (Exception e) {
				log.error("회원 조회 중 오류가 발생했습니다: {}", e.getMessage(), e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 조회 처리 중 오류가 발생했습니다.");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
		}

	}

	@Operation(summary = "Access Token 재발급", description = "Refresh Token을 바탕으로 Access Token을 재발급합니다.")
	@GetMapping(value = "/refresh")
	public ResponseEntity<?> generateAccessTokenFromRefreshToken(
			@Parameter(hidden = true) @RequestHeader("Authorization") String authHeader) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);

			String memberId = jwtUtil.getMemberIdFromToken(token);
			if (memberId != null && memberService.isRefreshTokenValid(memberId, token)) {
				String accessToken = jwtUtil.createAccessToken(memberId);
				String refreshToken = jwtUtil.createRefreshToken(memberId);

				log.debug("Access Token : {}", accessToken);
				log.debug("Refresh Token : {}", refreshToken);

				memberService.saveRefreshToken(memberId, refreshToken);

				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				status = HttpStatus.CREATED;
			} else {
				resultMap.put("message", "Refresh Token이 유효하지 않습니다.");
				status = HttpStatus.UNAUTHORIZED;
			}
		} else {
			resultMap.put("message", "Refresh Token이 유효하지 않습니다.");
			status = HttpStatus.UNAUTHORIZED;
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
		return ResponseEntity.status(status).headers(headers).body(resultMap);
	}

}
