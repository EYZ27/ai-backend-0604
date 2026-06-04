package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Book;

public record BookResponse(
        Long id, String title, String author, String category, String description
) {
    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getCategory(), book.getDescription());
    }
}
