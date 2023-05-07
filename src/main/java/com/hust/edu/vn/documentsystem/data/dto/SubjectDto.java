package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Subject} entity
 */
@Data
@Getter
@Setter
public class SubjectDto implements Serializable {
    private  Long id;
    private  String name;
    private  String subjectId;
    private  String description;
    private  Date createdAt;
}