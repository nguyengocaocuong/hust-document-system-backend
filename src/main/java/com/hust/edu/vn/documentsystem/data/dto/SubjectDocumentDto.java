package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SubjectDocument} entity
 */
@Data
public class SubjectDocumentDto implements Serializable {
    private Long id;
    private SubjectDocumentType subjectDocumentType;
    private DocumentType type;
    private DocumentDto document;
    private String description;
    private UserDto owner;
    private String semester;
    private List<CommentSubjectDocumentDto> comments = new ArrayList<>();
    private List<AnswerSubjectDocumentDto> answers = new ArrayList<>();
    private List<FavoriteSubjectDocumentDto> favorites = new ArrayList<>();
    private List<SharePrivateDto> shared;
    private Date createdAt;
    private Date lastEditedAt;
    private Date deletedAt;
    private boolean isPublic;
    private SubjectDto subject;

    @Data
    public static class SubjectDto implements Serializable {
        private  Long id;
        private  String institute;
        private  String name;
        private  String enName;
        private  Date createdAt;
        private String subjectCode;
    }
}