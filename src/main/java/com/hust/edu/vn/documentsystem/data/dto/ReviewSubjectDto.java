package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReviewSubject} entity
 */
@Data
@Setter
@Getter
public class ReviewSubjectDto implements Serializable {
    private  Long id;
    private  String review;
    private  boolean isDone;
    private  UserDto owner;
    private  SubjectDto subject;
    private  Date createdAt;
    private boolean isHidden;
    private ApproveType approved;
}