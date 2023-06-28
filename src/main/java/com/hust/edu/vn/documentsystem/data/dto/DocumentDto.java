package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Document} entity
 */
@Data
public class DocumentDto implements Serializable {
    private  Long id;
    private  Date createdAt;
    private  String name;
    private  String contentType;
    private  String thumbnail;
    private String path;
    private String url;
}