package com.ssafy.enjoytrip.messages.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MessageDto {
	
    private int messageId;

    @NotNull
    @Size(max = 16)
    private String senderId;

    @NotNull
    @Size(max = 16)
    private String receiverId;

    @NotNull
    private String content;

    private LocalDateTime createdAt;
}