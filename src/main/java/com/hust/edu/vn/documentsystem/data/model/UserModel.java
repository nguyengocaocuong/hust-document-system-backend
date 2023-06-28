package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserModel {
    private String userName;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @NotNull
    private String email;

//    private Date dob;

    private String phoneNumber;

    private MultipartFile avatarFile;
    private String avatar;
    private String password;
    private String matchingPassword;

    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;

    private String address;

    private RoleType roleType = RoleType.USER;
}
