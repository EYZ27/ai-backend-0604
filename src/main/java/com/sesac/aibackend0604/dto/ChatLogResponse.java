package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.ChatLog;

import java.time.LocalDateTime;

public record ChatLogResponse(
        Long id,
        Long userId,
        String username,
        String prompt,
        String response,
        LocalDateTime createdAt
) {
    public static ChatLogResponse from(ChatLog chatLog) {
        return new ChatLogResponse(
                chatLog.getId(),
                chatLog.getUser().getId(),
                null,
                chatLog.getPrompt(),
                chatLog.getResponse(),
                chatLog.getCreatedAt()
        );
    }

    public static ChatLogResponse fromWithUsername(ChatLog chatLog) {
        return new ChatLogResponse(
                chatLog.getId(),
                chatLog.getUser().getId(),
                chatLog.getUser().getUsername(),
                chatLog.getPrompt(),
                chatLog.getResponse(),
                chatLog.getCreatedAt()
        );
    }
}
