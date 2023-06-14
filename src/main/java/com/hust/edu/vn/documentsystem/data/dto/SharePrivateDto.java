package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SharePrivate} entity
 */
@Data
@Setter
@Getter
public class SharePrivateDto implements Serializable {
    private  Long id;
    private  UserDto user;
    private  Date sharedAt;
    private  Date expirationTime;
//    private SubjectDocumentAllDto subjectDocument;
}