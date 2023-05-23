package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.CommentReviewSubject} entity
 */
@Data
@Getter
@Setter
public class CommentReviewSubjectDto implements Serializable {
    private Long id;
    private String comment;
    private UserDto owner;
    private Date createdAt;
    private ReviewSubjectDto reviewSubject;
}