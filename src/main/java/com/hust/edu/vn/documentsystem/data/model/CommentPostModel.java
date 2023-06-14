package com.hust.edu.vn.documentsystem.data.model;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentPostModel {
    private String comment;
    private Long parentCommentId;
}
