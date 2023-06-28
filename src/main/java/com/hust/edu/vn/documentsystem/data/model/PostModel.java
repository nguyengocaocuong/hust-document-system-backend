package com.hust.edu.vn.documentsystem.data.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostModel {
    private Long id;
    private String description;
    @NotNull
    private Long subjectId;
    private MultipartFile[] documents;
    private Long done;
}
