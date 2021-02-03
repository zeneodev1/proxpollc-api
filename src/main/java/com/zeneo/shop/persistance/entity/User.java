package com.zeneo.shop.persistance.entity;

import com.zeneo.shop.model.ResponseUser;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    private String role;


    public enum Role {
        CUSTOMER,
        ADMIN
    }

    public ResponseUser toResponseUser() {
        return new ResponseUser(this.id, this.email);
    }

}
