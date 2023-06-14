package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SharePrivate} entity
 */
@Data
public class SharePrivateAllDto implements Serializable {
    private  Long id;
    private  SubjectDocumentAllDto subjectDocument;
    private  UserDto user;
    private  Date sharedAt;
}