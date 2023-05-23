package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentReviewSubjectModel {
    private Long id;

    private String comment;

    @NotNull
    private Long reviewSubjectId;
}
