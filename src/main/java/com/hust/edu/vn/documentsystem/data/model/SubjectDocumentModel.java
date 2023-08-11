package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDocumentModel {
    private Long id;
    private Long subjectDocumentTypeId;
    private DocumentType type;
    private MultipartFile[] documents;
    private String url;
    private String description;
    private Long isPublic = 0L;
    private String semester;
}
