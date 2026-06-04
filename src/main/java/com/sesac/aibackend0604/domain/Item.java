package com.sesac.aibackend0604.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    private Long id;
    private String name;
    private int price;
}
