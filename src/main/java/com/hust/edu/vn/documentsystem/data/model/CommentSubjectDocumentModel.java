package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentSubjectDocumentModel {
    private Long id;

    private String comment;

    @NotNull
    private Long subjectDocumentId;

    private Long ownerId;

    private Long parentCommentId;
}
