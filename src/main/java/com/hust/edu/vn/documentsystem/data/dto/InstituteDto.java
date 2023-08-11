package com.hust.edu.vn.documentsystem.data.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.Institute} entity
 */
@Data
public class InstituteDto implements Serializable {
    private Long id;
    private String institute;
    private String office;
    private String website;
    private String phoneNumber;
    private Date createdAt;
}