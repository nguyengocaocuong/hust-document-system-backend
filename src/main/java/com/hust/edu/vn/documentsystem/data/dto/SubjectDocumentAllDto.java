package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SubjectDocument} entity
 */
@Data
@Setter
@Getter
public class SubjectDocumentAllDto implements Serializable {
    private  Long id;
    private  SubjectDocumentType type;
    private  DocumentDto document;
    private  String description;
    private  UserDto owner;
    private  String semester;
    private SubjectDto subject;
    private List<CommentSubjectDocumentDto> commentSubjectDocumentList;
    private List<AnswerSubjectDocumentDto> answerSubjectDocumentList;
    private List<FavoriteSubjectDocumentDto> favoriteSubjectDocumentList;
}