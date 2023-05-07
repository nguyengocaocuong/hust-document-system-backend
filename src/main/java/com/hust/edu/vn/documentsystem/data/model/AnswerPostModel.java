package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerPostModel {
    private Long id;
    private String content;
    @NotNull
    private Long postId;

    private MultipartFile[] answerFile;
}
