package com.zeneo.shop.persistance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Product {

    @Id
    private String id;

    private String title;

    private String description;

    private String status;

    private List<String> Images;

    private List<String> tags;

    private Integer price;

    private Integer costPerItem;

    private String categoryId;

    private String departmentId;

    private Integer quantity;

    private boolean chargeTaxes;

    private String Condition;

    private String sold;

    private String shortCut;

    private ProductInformation information;
}
