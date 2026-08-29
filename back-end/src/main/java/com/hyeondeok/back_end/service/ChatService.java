package com.hyeondeok.back_end.service;

import com.hyeondeok.back_end.entity.Tutorial;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class ChatService {
    // AiConfig가 @Bean 으로 등록한 ChatClient 빈을 직접 주입
//    private final ChatClient openAiClient;
//    private final ChatClient ollamaClient;
    private final ChatClient chatClient;

//    public ChatService(
//            ChatClient openAiClient,
//            @Qualifier("ollamaChatClient") ChatClient ollamaClient,
//            ChatClient chatClient
//        ) {
//        this.openAiClient = openAiClient;
//        this.ollamaClient = ollamaClient;
//        this.chatClient = chatClient;
//    }

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

//    public String getOpenAiResponse(String query) {
//        return openAiClient.prompt()
//                .user(query)
//                .call()
//                .content();
//    }
//
//    public String getOllamaResponse(String query) {
//        return ollamaClient.prompt()
//                .user(query)
//                .call()
//                .content();
//    }

    public String chatWithSystemRole(String query) {
        return chatClient.prompt()
                .system("스포츠 전문가로서 답해줘.")
                .user(query)
                .call()
                .content();
    }

    public String simpleChat(String query) {
        Prompt prompt = new Prompt(query);
        return chatClient.prompt(prompt)
                .call()
                .content();
    }

    public String getDetailedContent(String query) {
        Prompt prompt = new Prompt(query);
        var content = chatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
        System.out.println(content);
        return content;
    }

    public String getMetaData(String query) {
        Prompt prompt = new Prompt(query);
        var metaData = chatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getMetadata();
        System.out.println(metaData);
        return metaData.toString();
    }

    public Tutorial getEntity(String query) {
        Prompt prompt = new Prompt(query);
        return chatClient.prompt(prompt)
                .call()
                .entity(Tutorial.class);
    }

    public List<String> getStringList(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .entity(new ParameterizedTypeReference<List<String>>() { });
    }

    public List<Tutorial> getTutorialList(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .entity(new ParameterizedTypeReference<List<Tutorial>>() { });
    }

    public String getSmartResponse(String query) {
        OpenAiChatOptions options;

        if (query.length() > 100) {
            options = OpenAiChatOptions.builder()
                    .model("gpt-4o")
                    .temperature(0.5)
                    .build();
        } else {
            options = OpenAiChatOptions.builder()
                    .model("gpt-4o-mini")
                    .temperature(0.8)
                    .build();
        }

        Prompt prompt = new Prompt(query, options);
        return chatClient.prompt(prompt).call().content();
    }

    public String getPriorityTestResponse(String query) {
            ChatOptions requestOptions = ChatOptions.builder()
                    .temperature(1.2)
                    .build();

            return chatClient.prompt(new Prompt(query, requestOptions))
                    .call()
                    .content();
    }
}
