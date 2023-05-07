package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Post} entity
 */
@Data
@Getter
@Setter
public class PostDto implements Serializable {
    private Long id;
    private UserDto owner;
    private String description;
    private String content;
    private DocumentDto document;
    private Date createdAt;
    private SubjectDto subject;
    private boolean isHidden;
}