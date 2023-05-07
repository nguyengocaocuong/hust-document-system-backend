package com.hust.edu.vn.documentsystem.data.model;

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
    private SubjectDocumentType type;
    private Long ownerId;
    @NotNull
    private Long subjectId;
    private MultipartFile[] documents;
    @NotNull
    private String description;
    private String contentText;
    private boolean isPublic = false;
}
