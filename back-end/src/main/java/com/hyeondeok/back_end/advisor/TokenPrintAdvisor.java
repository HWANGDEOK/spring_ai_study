package com.hyeondeok.back_end.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;


public class TokenPrintAdvisor implements CallAdvisor, StreamAdvisor {
    private Logger logger = LoggerFactory.getLogger(TokenPrintAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        // 전처리 단계
        this.logger.info("Requset: " + chatClientRequest.prompt().getContents());
        // 흐름 전달 단계
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        // 후처리 단계
        this.logger.info("모델로부터 응답을 받았습니다.");
        this.logger.info("응답 결과: " + chatClientResponse
                .chatResponse()
                .getResult()
                .getOutput()
                .getText());

        this.logger.info("요청(Request) 토큰: " + chatClientResponse
                .chatResponse()
                .getMetadata()
                .getUsage()
                .getPromptTokens());

        this.logger.info("응답(Response) 토큰: " + chatClientResponse
                .chatResponse()
                .getMetadata()
                .getUsage()
                .getCompletionTokens());

        this.logger.info("총 소모 토큰: " + chatClientResponse
                .chatResponse()
                .getMetadata()
                .getUsage()
                .getTotalTokens());


        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        Flux<ChatClientResponse> chatClientResponseFlux = streamAdvisorChain.nextStream(chatClientRequest);
        return chatClientResponseFlux;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    // Advisor의 우선 순위 설정 ( 낮을 수록 우선 순위가 높음 )
    public int getOrder() {
        return 0;
    }
}
