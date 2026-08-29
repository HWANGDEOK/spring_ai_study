package com.hyeondeok.back_end.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AiConfig {

//    @Bean
//    @Primary
//    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
//        return ChatClient.builder(openAiChatModel).build();
//    }
//
//    @Bean(name = "ollamaChatClient")
//    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
//        return ChatClient.builder(ollamaChatModel).build();
//    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.5))
                .build();
    }
}
