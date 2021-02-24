package com.zeneo.shop.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Wish {

    @Id
    private String id;

    private String userId;

    @DBRef
    private Product product;

    public Wish(String userId, Product product) {
        this.userId = userId;
        this.product = product;
    }
}
