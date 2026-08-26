package com.hyeondeok.back_end.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    // AiConfig가 @Bean 으로 등록한 ChatClient 빈을 직접 주입
    private final ChatClient chatClient;
    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getChatResponse(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .content();
    }
}
