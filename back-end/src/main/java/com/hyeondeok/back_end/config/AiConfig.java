package com.hyeondeok.back_end.config;

import com.hyeondeok.back_end.advisor.TokenPrintAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class AiConfig {

    @Bean
    @Primary
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.5))                      // 설정 우선순위
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),                      // 로그 기록하기
                        new TokenPrintAdvisor(),                        // 사용자 정의 Advisor
                        new SafeGuardAdvisor(List.of("games")))    // 단어 차단
                .build();
    }

    @Bean(name = "ollamaChatClient")
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel).build();
    }

//    @Bean
//    public ChatClient chatClient(ChatClient.Builder builder) {
//        return builder
//                .defaultOptions(ChatOptions.builder()
//                        .temperature(0.5))
//                .build();
//    }
}
