package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserModel {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String password;

    private Date dob;

    private String phoneNumber;

    @NotNull
    private String matchingPassword;

    private MultipartFile avatar;

    private Long id;
}
