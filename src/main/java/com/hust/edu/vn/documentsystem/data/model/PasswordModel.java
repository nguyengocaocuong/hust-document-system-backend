package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

@Data
@ToString
public class PasswordModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
