package com.zeneo.shop.persistance.entity;

import com.zeneo.shop.model.ShippingQuote;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class OrderItem {

    private Product product;

    private Integer quantity;

    private Float price;

    private ShippingQuote.ShippingMode shippingMode;

}
