package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.RoleType;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Data
@ToString
public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
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
    private String oldPassword;
    private String newPassword;
    private String username;
}
