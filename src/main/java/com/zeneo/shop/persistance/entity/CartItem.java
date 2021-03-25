package com.zeneo.shop.persistance.entity;

import com.zeneo.shop.model.ShippingQuote;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class CartItem {

    @Id
    private String id;

    @DBRef
    private Product product;

    private Integer quantity;

    private String userId;

    private ShippingQuote.ShippingMode shippingMode;
}
