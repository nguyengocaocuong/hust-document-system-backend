package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostModel {
    private Long id;
    @NotBlank
    private String description;
    private String content;
    @NotNull
    private Long subjectId;
    private MultipartFile[] documents;
}
