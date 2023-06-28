package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument} entity
 */
@Data
public class AnswerSubjectDocumentDto implements Serializable {
    private  Long id;
    private  String description;
    private DocumentType type;
    private DocumentDto document;
    private  Date createdAt;
    private  UserDto owner;
    private List<FavoriteAnswerSubjectDocumentDto> favorites;
}