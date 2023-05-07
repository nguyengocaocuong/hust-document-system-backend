package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasswordModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
