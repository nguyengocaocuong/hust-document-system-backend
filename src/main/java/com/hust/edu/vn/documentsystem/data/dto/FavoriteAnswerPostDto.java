package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost} entity
 */
@Data
public class FavoriteAnswerPostDto implements Serializable {
    private  Long id;
    private  UserDto user;
    private  Date createAt;
}