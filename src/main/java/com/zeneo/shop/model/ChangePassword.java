package com.zeneo.shop.model;

import lombok.Data;

@Data
public class ChangePassword {
    private String email;
    private String oldPassword;
    private String newPassword;
}
