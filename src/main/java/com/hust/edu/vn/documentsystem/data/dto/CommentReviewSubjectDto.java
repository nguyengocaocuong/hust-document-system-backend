package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.CommentReviewSubject} entity
 */
@Data
public class CommentReviewSubjectDto implements Serializable {
    private Long id;
    private String comment;
    private UserDto owner;
    private List<CommentReviewSubjectDto> childComment;
    private Date createdAt;
}