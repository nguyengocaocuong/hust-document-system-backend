package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SubjectDocumentSearchResult implements Serializable {
    private Long id;
    private DocumentType type;
    private String description;
    private Date createdAt;
    private String semester;
    private Long totalDownload;
    private Long totalView;
    private Long subjectDocumentTypeId;
    private String subjectDocumentTypeName;
    private String subjectDocumentType;
    private Long subjectId;;
    private String subjectName;
    private String subjectCode;
    private String institute;
    private Long totalComments;
    private Long totalAnswers;
    private Long totalFavorites;
    private Long ownerId;
    private String ownerFirstName;
    private String ownerLastName;
    private String thumbnail;

}
