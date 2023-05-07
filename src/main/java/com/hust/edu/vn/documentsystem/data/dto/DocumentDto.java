package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Document} entity
 */
@Data
@Getter
@Setter
public class DocumentDto implements Serializable {
    private  Long id;
    private  String content;
    private  DocumentType type;
    private  Date createdAt;
    private  String name;
    private  String contentType;
}