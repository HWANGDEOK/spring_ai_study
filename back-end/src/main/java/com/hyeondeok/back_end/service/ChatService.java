package com.hyeondeok.back_end.service;

import com.hyeondeok.back_end.entity.Tutorial;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    // AiConfig가 @Bean 으로 등록한 ChatClient 빈을 직접 주입
    private final ChatClient openAiClient;
    private final ChatClient ollamaClient;


    @Value("classpath:prompts/system-message.st")
    private Resource systemMessageResource;
    @Value("classpath:prompts/user-message.st")
    private Resource userMessageResource;


    public ChatService(
            ChatClient openAiClient,
            @Qualifier("ollamaChatClient") ChatClient ollamaClient,
            ChatClient chatClient
        ) {
        this.openAiClient = openAiClient;
        this.ollamaClient = ollamaClient;
//        this.chatClient = chatClient;
    }

//    public ChatService(ChatClient chatClient) {
//        this.chatClient = chatClient;
//    }

    public String getOpenAiResponse(String query) {
        return openAiClient.prompt()
                .user(query)
                .call()
                .content();
    }

    public String getOllamaResponse(String query) {
        return ollamaClient.prompt()
                .user(query)
                .call()
                .content();
    }

    // 프롬프트 구성
    public String chatWithSystemRole(String query) {
        return openAiClient.prompt()
                .system("스포츠 전문가로서 답해줘.")
                .user(query)
                .call()
                .content();
    }

    // 프롬프트 객체 생성
    public String simpleChat(String query) {
        Prompt prompt = new Prompt(query);
        return openAiClient.prompt(prompt)
                .call()
                .content();
    }

    // 상세 내용 추출
    public String getDetailedContent(String query) {
        Prompt prompt = new Prompt(query);
        var content = openAiClient.prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
        System.out.println(content);
        return content;
    }

    // 메타 데이터 추출
    public String getMetaData(String query) {
        Prompt prompt = new Prompt(query);
        var metaData = openAiClient.prompt(prompt)
                .call()
                .chatResponse()
                .getMetadata();
        System.out.println(metaData);
        return metaData.toString();
    }

    // 자바 객체로 변환해서 받기
    public Tutorial getEntity(String query) {
        Prompt prompt = new Prompt(query);
        return openAiClient.prompt(prompt)
                .call()
                .entity(Tutorial.class);
    }

    // 객체 리스트로 받기
    public List<String> getStringList(String query) {
        return openAiClient.prompt()
                .user(query)
                .call()
                .entity(new ParameterizedTypeReference<List<String>>() { });
    }

    public List<Tutorial> getTutorialList(String query) {
        return openAiClient.prompt()
                .user(query)
                .call()
                .entity(new ParameterizedTypeReference<List<Tutorial>>() { });
    }


    // 요청별 옵션 설정
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
        return openAiClient.prompt(prompt).call().content();
    }

    // ChatOptions 설정 우선순위 확인
    public String getPriorityTestResponse(String query) {
            ChatOptions requestOptions = ChatOptions.builder()
                    .temperature(1.2)
                    .build();

            return openAiClient.prompt(new Prompt(query, requestOptions))
                    .call()
                    .content();
    }


    // 동적 프롬프트(유창한 api)
    public String getExpertResponse(String query) {
        String queryStrTemplate = "당신은 코딩 및 프로그래밍 전문가 입니다." +
                "항상 자바로 프로그램을 작성하세요" +
                "이제 이 질문에 답변하세요: {query}";

        return openAiClient.prompt()
                .user(u -> u.text(queryStrTemplate).param("query", query))
                .call()
                .content();
    }


    // 동적 프롬프트(명시적)
    public String getExplicitTemplateResponse(String subject, String example) {
        PromptTemplate strTemplate = PromptTemplate.builder()
                .template("{subject} 주제에서, {example} 예시를 들어주세요")
                .build();

        String renderedMessage = strTemplate.render(Map.of("subject", subject, "example", example));
        Prompt prompt = new Prompt(renderedMessage);

        return openAiClient.prompt(prompt).call().content();
    }

    // 역할기반 명시적 프롬프트
    public String getRoleBasedTemplateResponse(String subject, String example) {
        String systemText = "당신은 {subject} 전문가입니다. 항상 전문적인 관점에서 답변하세요.";
        SystemPromptTemplate systemTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemTemplate.createMessage(Map.of("subject", subject));

        String userText = "그것과 관련하여 {example} 을(를) 예제와 함께 설명해주세요.";
        PromptTemplate userTemplate = new PromptTemplate(userText);
        Message userMessage = userTemplate.createMessage(Map.of("example",example));

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return openAiClient.prompt(prompt).call().content();
    }

    // 외부 파일로 프롬프트 관리
    public String getExternalTemplateResponse(String concept) {
        return openAiClient.prompt()
                .system(systemMessageResource)
                .user(u -> u.text(userMessageResource)
                        .param("concept", concept)
                )
                .call()
                .content();
    }
}
