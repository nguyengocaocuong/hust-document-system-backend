package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher} entity
 */
@Data
@Getter
@Setter
public class FavoriteReviewTeacherDto implements Serializable {
    private  UserDto user;
    private  Date createAt;
    private Long id;
}