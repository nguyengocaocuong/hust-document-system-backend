package com.hust.edu.vn.documentsystem.data.model;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDocumentModel {
    private Long id;
    @NotNull
    private SubjectDocumentType subjectDocumentType;
    private DocumentType type;
    private MultipartFile[] documents;
    private String url;
    @NotNull
    private String description;
    private Long isPublic = 0L;
}
