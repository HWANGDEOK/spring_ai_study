package com.hyeondeok.back_end.config;

import com.hyeondeok.back_end.advisor.TokenPrintAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
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
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory) {

        MessageChatMemoryAdvisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .build();


        return ChatClient.builder(openAiChatModel)
                .defaultOptions(ChatOptions.builder()
                        .temperature(0.5))                      // 설정 우선순위
                .defaultAdvisors(
                        memoryAdvisor,                                  // chatMemory
                        new SimpleLoggerAdvisor(),                      // 로그 기록하기
                        new TokenPrintAdvisor(),                        // 사용자 정의 Advisor
                        new SafeGuardAdvisor(List.of("games")))    // 단어 차단
//                .defaultSystem("You are a helpful coding assistant")
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

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository) // DB저장소 연결
                .maxMessages(15)        // 최근 15개만 메모리로 사용하도록 설정 ( 기본 20개 )
                .build();
    }
}
