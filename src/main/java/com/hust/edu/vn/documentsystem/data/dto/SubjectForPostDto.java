package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Subject} entity
 */
@Data
public class SubjectForPostDto implements Serializable {
    private Long id;
    private String name;
    private String subjectCode;
}