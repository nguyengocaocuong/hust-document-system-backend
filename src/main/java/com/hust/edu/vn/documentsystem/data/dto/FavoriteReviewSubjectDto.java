package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject} entity
 */
@Data
public class FavoriteReviewSubjectDto implements Serializable {
    private Long id;
    private UserDto user;
    private Date createAt;
}