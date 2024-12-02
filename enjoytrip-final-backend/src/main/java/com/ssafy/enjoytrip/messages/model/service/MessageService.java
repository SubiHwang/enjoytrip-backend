package com.ssafy.enjoytrip.messages.model.service;

import java.util.List;

import com.ssafy.enjoytrip.messages.model.MessageDto;
import com.ssafy.enjoytrip.messages.model.MessageRequest;

public interface MessageService {

	List<MessageDto> getAllMessages(String tokenUserId);

	int addMessages(String tokenUserId,MessageRequest messageRequest);

	int deleteMessages(String tokenUserId, int messageId);

}
