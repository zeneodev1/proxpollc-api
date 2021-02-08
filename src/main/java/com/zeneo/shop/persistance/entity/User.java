package com.zeneo.shop.persistance.entity;

import com.zeneo.shop.model.ResponseUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String fullName;

    private String email;

    private String password;

    private String role;


    public enum Role {
        CUSTOMER,
        ADMIN
    }

    public User(String id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public ResponseUser toResponseUser() {
        return new ResponseUser(this.id, this.email);
    }

}
