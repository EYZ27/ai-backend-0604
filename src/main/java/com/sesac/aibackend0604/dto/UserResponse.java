package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Role;
import com.sesac.aibackend0604.domain.User;

public record UserResponse(
        Long id,
        String username,
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
