package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.entity.History;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link History} entity
 */
@Data
public class HistoryDto implements Serializable {
    private  Long id;
    private  UserDto user;
    private  SubjectDocumentDto subjectDocument;
    private Date createdAt;

    /**
     * A DTO for the {@link SubjectDocument} entity
     */
    @Data
    public static class SubjectDocumentDto implements Serializable {
        private  Long id;
        private SubjectDocumentTypeDto subjectDocumentType;
        private  DocumentType type;
        private  String description;
        private  String descriptionEn;
        private  boolean isPublic;
        private  Date createdAt;
        private  Date deletedAt;
        private  boolean isDelete;
        private UserDto owner;
        private DocumentDto document;
    }
}