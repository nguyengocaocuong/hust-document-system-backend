package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentReviewTeacherModel {
    private Long id;

    private String comment;

    private int rating;

    @NotNull
    private Long reviewTeacherId;
}
