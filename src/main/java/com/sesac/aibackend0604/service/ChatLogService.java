package com.sesac.aibackend0604.service;

import com.sesac.aibackend0604.domain.ChatLog;
import com.sesac.aibackend0604.domain.User;
import com.sesac.aibackend0604.error.NotFoundException;
import com.sesac.aibackend0604.repository.ChatLogRepository;
import com.sesac.aibackend0604.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatLogService {

    private final UserRepository userRepository;
    private final ChatLogRepository chatLogRepository;

    @Transactional
    public ChatLog save(String username, String prompt, String response) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> NotFoundException.of("user", username));
        return chatLogRepository.save(
                ChatLog.builder()
                        .user(user)
                        .prompt(prompt)
                        .response(response)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public List<ChatLog> findByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<ChatLog> findByUserIdWithUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> NotFoundException.of("user", userId));
        return chatLogRepository.findByUserIdWithUser(userId);
    }
}
