package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher} entity
 */
@Data
@Setter
@Getter
public class CommentReviewTeacherDto implements Serializable {
    private  Long id;
    private  String comment;
    private  int rating;
    private  UserDto owner;
    private  Date createdAt;
    private  ReviewTeacherDto reviewTeacher;
}