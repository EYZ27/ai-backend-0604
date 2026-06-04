package com.sesac.aibackend0604.error;

import java.time.Instant;

public record ErrorResponse(String code, String message, Instant timestamp) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, Instant.now());
    }
}