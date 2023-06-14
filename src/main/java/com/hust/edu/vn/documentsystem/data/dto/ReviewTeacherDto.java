package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReviewTeacher} entity
 */
@Data
@Setter
@Getter
public class ReviewTeacherDto implements Serializable {
    private  Long id;
    private  String review;
    private  boolean done;
    private  UserDto owner;
    private  TeacherDto teacher;
    private  Date createdAt;
    private boolean hidden;
    private ApproveType approved;
    private List<FavoriteReviewTeacherDto> favorites;
    private List<CommentReviewTeacherDto> comments;
}