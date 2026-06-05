package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Role;
import com.sesac.aibackend0604.domain.User;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String username,
        @NotBlank String password
) {
    public User toEntity() {
        return User.builder().username(username).passwordHash(password).role(Role.USER).build();
    }
}
