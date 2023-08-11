package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReportDuplicateSubjectDocument} entity
 */
@Data
public class ReportDuplicateSubjectDocumentDto implements Serializable {
    private  Long id;
    private  String content;
    private  LocalDateTime createdAt;
    private  UserDto owner;
    private  ReportStatus status;
    private  String message;
    private  String processMessage;
    private  SubjectDocumentDto subjectDocumentFirst;
    private  SubjectDocumentDto subjectDocumentSecond;

    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SubjectDocument} entity
     */
    @Data
    public static class SubjectDocumentDto implements Serializable {
        private  Long id;
        private SubjectDocumentTypeDto subjectDocumentType;
        private  DocumentType type;
        private  String description;
        private  String descriptionEn;
        private  boolean isPublic;
        private  String semester;
        private  Date createdAt;
        private  Date deletedAt;
        private  boolean isDelete;
        private DocumentDto document;
    }
}