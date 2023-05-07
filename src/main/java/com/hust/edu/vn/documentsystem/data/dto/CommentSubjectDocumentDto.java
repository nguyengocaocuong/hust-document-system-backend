package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.CommentSubjectDocument} entity
 */
@Data
@Getter
@Setter
public class CommentSubjectDocumentDto implements Serializable {
    private  Long id;
    private  String comment;
    private  Integer rating;
    private  UserDto owner;
    private  SubjectDocumentDto subjectDocument;
    private  Date created_at;
}