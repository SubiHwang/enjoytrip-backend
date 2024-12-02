package com.ssafy.enjoytrip.messages.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
public class MessageRequest {
	
	private String receiverId;
	private String content;

}
