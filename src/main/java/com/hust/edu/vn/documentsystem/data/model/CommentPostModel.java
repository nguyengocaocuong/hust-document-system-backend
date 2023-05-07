package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentPostModel {
    private Long id;

    private String comment;

    private int rating;

    @NotNull
    private Long postId;

}
