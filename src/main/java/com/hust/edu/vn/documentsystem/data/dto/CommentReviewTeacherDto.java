package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher} entity
 */
@Data
public class CommentReviewTeacherDto implements Serializable {
    private  Long id;
    private  String comment;
    private  UserDto owner;
    private  Date createdAt;
    private List<CommentReviewTeacherDto> childComment;
}