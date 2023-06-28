package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link FavoritePost} entity
 */
@Data
public class FavoritePostDto implements Serializable {
    private  Long id;
    private  UserDto user;
    private  Date createAt;
}