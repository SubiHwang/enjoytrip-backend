package com.ssafy.enjoytrip.messages.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.exception.CustomException;
import com.ssafy.enjoytrip.exception.ErrorCode;
import com.ssafy.enjoytrip.messages.model.MessageDto;
import com.ssafy.enjoytrip.messages.model.MessageRequest;
import com.ssafy.enjoytrip.messages.model.mapper.MessageMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

	private final MessageMapper messageMapper;

	@Override
	public List<MessageDto> getAllMessages(String tokenUserId) {
		if (messageMapper.getUserById(tokenUserId) == null) {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}

		List<MessageDto> messages = messageMapper.getAllMessages(tokenUserId);

		return messages;
	}

	@Override
	public int addMessages(String senderId, MessageRequest messageRequest) {

		// 디버깅용 로그
		log.info("발신자: {}, 수신자: {}, 내용: {}", senderId, messageRequest.getReceiverId(), messageRequest.getContent());

		if (messageMapper.getUserById(senderId) == null) {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}

		System.out.println(senderId);

		// 디버깅용 로그
		log.info("발신자: {}, 수신자: {}, 내용: {}", senderId, messageRequest.getReceiverId(), messageRequest.getContent());

		MessageDto messageDTO = MessageDto.builder().senderId(senderId).receiverId(messageRequest.getReceiverId())
				.content(messageRequest.getContent()).build();

		// MessageDto 생성 후 로그
		log.info("생성된 MessageDto: {}", messageDTO);

		return messageMapper.addMessages(messageDTO);
	}

	@Override
	public int deleteMessages(String receiverId, int messageId) {
		
		System.out.println(messageId);

		if (messageMapper.getUserById(receiverId) == null) {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}

		return messageMapper.deleteMessages(messageId);
	}

}
