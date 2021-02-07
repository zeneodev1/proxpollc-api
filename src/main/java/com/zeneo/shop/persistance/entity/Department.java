package com.zeneo.shop.persistance.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@ToString
public class Department {

    @Id
    private String id;

    private String name;

    private String description;

    private List<String> images;

    @DBRef
    private List<Category> categories;

    private Integer productCount;

    private String[] tags;

}
