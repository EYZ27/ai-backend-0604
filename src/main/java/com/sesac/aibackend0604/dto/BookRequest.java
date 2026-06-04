package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Book;
import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        String title,
        String author,
        String category,
        String description
) {
    public Book toEntity() {
        return Book.builder().title(title).author(author).category(category).description(description).build();
    }
}
