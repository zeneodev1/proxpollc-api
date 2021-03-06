package com.zeneo.shop.persistance.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Category {

    @Id
    private String id;

    private String name;

    private String description;

    private String departmentId;

    private Integer productCount;

    private String[] tags;

    private String shortCut;
}
