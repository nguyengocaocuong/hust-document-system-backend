package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;

@Data
@AllArgsConstructor
public class CommentReviewSubjectModel {
    private Long id;

    private String comment;

    private Long parentCommentId;
}
