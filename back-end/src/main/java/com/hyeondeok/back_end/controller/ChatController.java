package com.hyeondeok.back_end.controller;

import com.hyeondeok.back_end.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "query", defaultValue = "안녕") String query) {

        String response = chatService.getChatResponse(query);

        return ResponseEntity.ok(response);
    }
}
