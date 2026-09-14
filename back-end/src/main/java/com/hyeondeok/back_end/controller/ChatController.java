package com.hyeondeok.back_end.controller;

import com.hyeondeok.back_end.entity.Tutorial;
import com.hyeondeok.back_end.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 오픈AI 호출용 엔드포인트
//    @GetMapping("/chat/openai")
//    public ResponseEntity<String>
//        chatWithOpenAi(@RequestParam(value = "q", defaultValue = "안녕") String query) {
//        String response = chatService.getOpenAiResponse(query);
//        return ResponseEntity.ok(response);
//    }
//
//    // 올라마 호출용 엔드포인트
//    @GetMapping("/chat/ollama")
//    public ResponseEntity<String>
//    chatWithOllama(@RequestParam(value = "q", defaultValue = "안녕") String query) {
//        String response = chatService.getOllamaResponse(query);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/chat")
    public String chat(@RequestParam String query) {
        return chatService.getPriorityTestResponse(query);
    }

    @GetMapping("/chat/template")
    public ResponseEntity<String> chatWithTemplate(@RequestParam String query) {
        String response = chatService.getExpertResponse(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/explicit")
    public ResponseEntity<String> chatWithExplicitTemplate(
            // 사용할 주제
            @RequestParam(defaultValue = "Spring Framework") String subject,
            // 예시 데이터
            @RequestParam(defaultValue = "Spring @Controller example") String example
    ) {
        // 사용자 입력 파라미터
        String response = chatService.getExplicitTemplateResponse(subject, example);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/role-dynamic")
    public ResponseEntity<String> chatWithRoleTemplate(
            @RequestParam(defaultValue = "자바") String subject,
            @RequestParam(defaultValue = "람다 스트림") String example
    ) {
        String response = chatService.getRoleBasedTemplateResponse(subject, example);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/external")
    public ResponseEntity<String> chatWithExternalTemplate(
            @RequestParam(defaultValue = "Spring Framework validation") String concept) {

        String response = chatService.getExternalTemplateResponse(concept);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/stream")
    public ResponseEntity<Flux<String>> streamChat(@RequestParam String query) {

        Flux<String> response = chatService.streamChat(query);

        return ResponseEntity.ok(response);
    }


}
