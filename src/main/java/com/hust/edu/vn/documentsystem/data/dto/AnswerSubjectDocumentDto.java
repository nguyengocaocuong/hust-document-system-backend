package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument} entity
 */
@Data
@Setter
@Getter
public class AnswerSubjectDocumentDto implements Serializable {
    private  Long id;
    private  String content;
    private  String type;
    private  String name;
    private  Date createdAt;
    private  SubjectDocumentDto subjectDocument;
    private  UserDto owner;
}