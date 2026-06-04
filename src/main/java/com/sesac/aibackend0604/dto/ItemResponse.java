package com.sesac.aibackend0604.dto;

import com.sesac.aibackend0604.domain.Item;

public record ItemResponse(Long id, String name, int price) {

    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getPrice());
    }
}
