package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.SubjectDocumentType} entity
 */
@Data
public class SubjectDocumentTypeDto implements Serializable {
    private Long id;
    private String type;
    private String name;
    private Date createdAt;
}