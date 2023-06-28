package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

@Data
public class CommentReviewTeacherModel {
    private Long id;

    private String comment;

    private Long parentCommentId;
}
