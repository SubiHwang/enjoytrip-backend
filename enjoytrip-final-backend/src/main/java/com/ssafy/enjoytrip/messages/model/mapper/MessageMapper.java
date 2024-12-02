package com.ssafy.enjoytrip.messages.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.enjoytrip.messages.model.MessageDto;
import com.ssafy.enjoytrip.messages.model.MessageRequest;

@Mapper
public interface MessageMapper {


	List<MessageDto> getAllMessages(String receiverId);

	String getUserById(String userId);

	int addMessages(MessageDto messageDto);

	int deleteMessages(int messageId);

}
