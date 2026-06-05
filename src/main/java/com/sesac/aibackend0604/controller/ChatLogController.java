package com.sesac.aibackend0604.controller;

import com.sesac.aibackend0604.domain.ChatLog;
import com.sesac.aibackend0604.dto.ChatLogRequest;
import com.sesac.aibackend0604.dto.ChatLogResponse;
import com.sesac.aibackend0604.repository.ChatLogRepository;
import com.sesac.aibackend0604.service.ChatLogService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chat-logs")
@RequiredArgsConstructor
public class ChatLogController {

    private final ChatLogService chatLogService;

    @GetMapping
    public List<ChatLogResponse> list(@RequestParam Long userId) {
        return chatLogService.findByUserId(userId).stream().map(ChatLogResponse::from).toList();
    }

    @GetMapping("/with-user")
    public List<ChatLogResponse> listWithUser(@RequestParam Long userId) {
        return chatLogService.findByUserIdWithUser(userId).stream().map(ChatLogResponse::fromWithUsername).toList();
    }

    @PostMapping
    public ResponseEntity<ChatLogResponse> create(@Valid @RequestBody ChatLogRequest req) {
        ChatLog saved = chatLogService.save(req.userId(), req.prompt(), req.response());
        URI location = URI.create("/chat-logs" + saved.getId());
        return ResponseEntity.created(location).body(ChatLogResponse.from(saved));
    }

}
