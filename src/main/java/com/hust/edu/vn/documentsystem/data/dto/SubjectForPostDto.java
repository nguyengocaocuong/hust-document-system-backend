package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Subject} entity
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class SubjectForPostDto implements Serializable {
    private Long id;
    private String name;
    private String subjectCode;
}