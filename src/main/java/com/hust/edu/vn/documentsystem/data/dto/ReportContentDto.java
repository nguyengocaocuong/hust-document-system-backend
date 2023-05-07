package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReportContent} entity
 */
@Data
@Getter
@Setter
public class ReportContentDto implements Serializable {
    private  Long id;
    private  String content;
    private  Date createdAt;
    private  ReportStatus status;
    private  String message;
    private  UserDto owner;
    private  DocumentDto document;
}