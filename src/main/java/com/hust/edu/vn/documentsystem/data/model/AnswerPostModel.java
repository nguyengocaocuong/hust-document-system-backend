package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerPostModel {
    private Long id;
    private String description;
    @Nullable
    private MultipartFile[] documents;
    private DocumentType type = DocumentType.PDF;
    private String url;
}
