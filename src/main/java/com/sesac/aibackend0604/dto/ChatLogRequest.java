package com.sesac.aibackend0604.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatLogRequest(
        @NotNull Long userId,
        @NotBlank String prompt,
        String response
) {
}
