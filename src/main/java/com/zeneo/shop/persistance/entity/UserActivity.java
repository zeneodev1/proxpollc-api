package com.zeneo.shop.persistance.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document
@NoArgsConstructor
public class UserActivity {

    @Id
    private String id;

    private String userId;

    private Set<CartItem> cartItems = new HashSet<CartItem>();

    @DBRef
    private Set<Product> wishList = new HashSet<>();

    public UserActivity(String userId) {
        this.userId = userId;
    }
}
