package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.User} entity
 */
@Data
@Getter
@Setter
public class UserDto implements Serializable {
    private  Long id;
    private  String lastName;
    private  String firstName;
    private  String email;
    private  Date dob;
    private  String avatar;
    private  Date createdAt;
    private  String phoneNumber;
    private String token;
}