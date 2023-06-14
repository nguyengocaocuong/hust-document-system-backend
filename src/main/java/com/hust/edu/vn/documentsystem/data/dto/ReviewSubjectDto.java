package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReviewSubject} entity
 */
@Data
@Setter
@Getter
@NoArgsConstructor
public class ReviewSubjectDto implements Serializable {
    private  Long id;
    private  String review;
    private  boolean isDone;
    private  UserDto owner;
    private  SubjectDto subject;
    private  Date createdAt;
    private boolean isHidden;
    private ApproveType approved;
    private List<FavoriteReviewSubjectDto> favorites;
    private List<CommentReviewSubjectDto> comments;
}