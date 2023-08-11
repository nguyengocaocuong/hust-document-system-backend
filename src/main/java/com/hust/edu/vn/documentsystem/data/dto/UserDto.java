package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.RoleType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.User} entity
 */
@Data
public class UserDto implements Serializable {
    private  Long id;
    private  String lastName;
    private  String firstName;
    private  String email;
    private  Date dob;
    private  String avatar;
    private  Date createdAt;
    private  String phoneNumber;

    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String token;

    private RoleType roleType;
    private boolean isEnable;
    private boolean isSetup;

    private String username;
    private String address;
}