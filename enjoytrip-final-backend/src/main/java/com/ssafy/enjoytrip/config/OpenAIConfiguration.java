package com.ssafy.enjoytrip.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OpenAIConfiguration {

	@Value("${spring.ai.openai.api-key}")
	private String apiKey;

	@Bean
	public OpenAiApi openAiApi() {

		return new OpenAiApi(apiKey);
	}

	@PostConstruct
	public void checkApiKey() {
		log.info("API Key: {}", apiKey); // 실제 운영환경에서는 로그에 API 키를 출력하면 안됨!
	}

	@Bean
	public ChatClient chatClient(OpenAiApi openAiApi) {
		return new OpenAiChatClient(openAiApi);
	}

}
