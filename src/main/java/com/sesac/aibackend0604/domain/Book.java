package com.sesac.aibackend0604.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private Long id;
    private String title;
    private String author;
    private String category;
    private String description;
}
