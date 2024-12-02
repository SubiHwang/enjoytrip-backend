package com.ssafy.enjoytrip.messages.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoytrip.exception.CustomException;
import com.ssafy.enjoytrip.exception.ErrorCode;
import com.ssafy.enjoytrip.messages.model.MessageDto;
import com.ssafy.enjoytrip.messages.model.MessageRequest;
import com.ssafy.enjoytrip.messages.model.service.MessageService;
import com.ssafy.enjoytrip.util.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/messages")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "쪽지 컨트롤러", description = "쪽지 CRUD를 관리하는 컨트롤러")
public class MessageController {

	private final JWTUtil jwtUtil;
	private final MessageService messageService;

	@Operation(summary = "쪽지 조회", description = "쪽지를 조회합니다.")
	@GetMapping("/all")
	public ResponseEntity<List<MessageDto>> getAllMessages(
			@RequestHeader("Authorization") String authHeader) {
		log.info("쪽지 조회 요청");

		try {
			String token = authHeader.substring(7);
			String tokenUserId = jwtUtil.getMemberIdFromToken(token);

			List<MessageDto> messages = messageService.getAllMessages(tokenUserId);
			return ResponseEntity.ok(messages);
		} catch (CustomException e) {
			// 커스텀 예외를 그대로 상위로 전달
			// 서비스에서 발생한 NOT_FOUND, FORBIDDEN 등의 예외를 그대로 유지
			throw e;
		} catch (Exception e) {
			log.error("쪽지 조회 실패: ", e);
			throw new CustomException(ErrorCode.SERVER_ERROR);
			// 예상치 못한 일반 예외 처리
		}
	}

	@Operation(summary = "쪽지 작성", description = "쪽지를 작성합니다.")
	@PostMapping
	public ResponseEntity<?> addMessages(@RequestHeader("Authorization") String authHeader,
			@RequestBody MessageRequest messageRequest) {

		log.info("쪽지 작성 요청");

		log.info("수신된 데이터 - receiverId: {}, content: {}", messageRequest.getReceiverId(), messageRequest.getContent());

		try {
			String token = authHeader.substring(7);
			String tokenUserId = jwtUtil.getMemberIdFromToken(token);

			int result = messageService.addMessages(tokenUserId, messageRequest);

			if (result > 0) {
				return ResponseEntity.ok().build(); // 여기서 바로 return
			}
			return ResponseEntity.badRequest().build(); // 실패시 400 반환
		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			log.error("쪽지 작성 실패: ", e);
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}
	}

	@Operation(summary = "쪽지 삭제", description = "쪽지를 삭제합니다.")
	@DeleteMapping("/{messageId}")
	public ResponseEntity<?> removeMessages(@RequestHeader("Authorization") String authHeader,
			@PathVariable int messageId) {

		log.info("쪽지 삭제 요청");

		try {

			String token = authHeader.substring(7);
			String tokenUserId = jwtUtil.getMemberIdFromToken(token);

			int result = messageService.deleteMessages(tokenUserId, messageId);

			if (result > 0) {
				return ResponseEntity.ok().build();
			}
			return ResponseEntity.badRequest().build();

		} catch (CustomException e) {
			throw e;
		} catch (Exception e) {
			log.error("쪽지 삭제 실패: ", e);
			throw new CustomException(ErrorCode.SERVER_ERROR);
		}


	}

}
