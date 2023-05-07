package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReportDuplicateDocument} entity
 */
@Data
public class ReportDuplicateDocumentDto implements Serializable {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDto owner;
    private ReportStatus status;
    private String message;
    private DocumentDto documentFirst;
    private DocumentDto documentSecond;
}