package com.zeneo.shop.persistance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "product")
@Data
public class ProductDetails {

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

    private String condition;

    private String sold;

    private String shortCut;

    @DBRef(lazy = true)
    private ProductDescription productDescription;

    private ProductInformation information;


}
