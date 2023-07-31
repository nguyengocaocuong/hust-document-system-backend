package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReviewSubject} entity
 */
@Data
public class ReviewSubjectDto implements Serializable {
    private  Long id;
    private  String review;
    private  boolean done;
    private  UserDto owner;
    private  SubjectDto subject;
    private  Date createdAt;
    private boolean isHidden;
    private ApproveType approved;
    private List<FavoriteReviewSubjectDto> favorites;
    private List<CommentReviewSubjectDto> comments;
}