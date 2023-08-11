package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.entity.Institute;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.SubjectDocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Subject} entity
 */
@Data
@NoArgsConstructor
public class SubjectDto implements Serializable {
    private Long id;
    private String name;
    private String enName;
    private InstituteDto institute;
    private String description;
    private Date createdAt;
    private String subjectCode;
    private Long totalComment = 0L;
    private Long totalDocument = 0L;
    private Long totalFavorite = 0L;
    private Long totalAnswer = 0L;
    private List<UserDto> enrollment = new ArrayList<>();
    private List<SubjectDocumentDto> subjectDocuments = new ArrayList<>();

    @Data
    public static class SubjectDocumentDto implements Serializable {
        private Long id;
        private SubjectDocumentType subjectDocumentType;
        private DocumentType type;
        private DocumentDto document;
        private String description;
        private UserDto owner;
        private List<CommentSubjectDocumentDto> comments = new ArrayList<>();
        private List<AnswerSubjectDocumentDto> answers = new ArrayList<>();
        private List<FavoriteSubjectDocumentDto> favorites = new ArrayList<>();
        private List<SharePrivateDto> shared;
        private Date createdAt;
        private Date deletedAt;
        private boolean isPublic;
    }

    public SubjectDto(Long id, String name, String subjectCode, Institute institute) {
        this.id = id;
        this.name = name;
        this.subjectCode = subjectCode;
        InstituteDto instituteDto = new InstituteDto();
        instituteDto.setId(institute.getId());
        instituteDto.setInstitute(institute.getInstitute());
        this.institute = instituteDto;
    }

    public SubjectDto(Subject subject, Long totalDocument, Long totalComment, Long totalFavorite, Long totalAnswer) {
        this.id = subject.getId();
        InstituteDto instituteDto = new InstituteDto();
        instituteDto.setInstitute(subject.getInstitute().getInstitute());
        instituteDto.setId(subject.getInstitute().getId());
        this.institute = instituteDto;
        this.name = subject.getName();
        this.description = subject.getDescription();
        this.createdAt = subject.getCreatedAt();
        this.totalDocument = totalDocument;
        this.totalComment = 0L;
        this.totalFavorite = totalFavorite;
        this.totalAnswer = 0L;
        this.subjectCode = subject.getSubjectCode();
        this.subjectDocuments = null;
    }
}