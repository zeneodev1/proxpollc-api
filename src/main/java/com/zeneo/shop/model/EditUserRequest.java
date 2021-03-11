package com.zeneo.shop.model;

import lombok.Data;

@Data
public class EditUserRequest {
    private String firstName;
    private String lastName;
    private String Email;
    private String currentPassword;
    private String newPassword;
}
