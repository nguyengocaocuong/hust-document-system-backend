package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerSubjectDocumentModel {
    private Long id;
    private String description;
    private DocumentType type;
    private MultipartFile[] documents;
    private String url;
}
