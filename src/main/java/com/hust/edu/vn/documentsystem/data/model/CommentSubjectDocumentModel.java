package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentSubjectDocumentModel {
    private Long id;

    private String comment;

    @Size(min = 0, max = 5)
    private int rating = 3;

    @NotNull
    private Long subjectDocumentId;
}
