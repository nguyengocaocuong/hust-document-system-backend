package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class CommentSubjectDocumentModel {
    private Long id;

    private String comment;

    @NotNull
    private Long subjectDocumentId;

    private Long ownerId;

    private Long parentCommentId;
}
