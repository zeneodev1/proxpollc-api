package com.zeneo.shop.persistance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Order {

    @Id
    private String id;

    private List<OrderItem> orderItems;

    private Float totalPrice;

    private String customerID;

    private String trackId;

}
