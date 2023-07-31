package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import com.hust.edu.vn.documentsystem.entity.ReportContentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link ReportContentReviewSubject} entity
 */
@Data
public class ReportContentReviewSubjectDto implements Serializable {
    private  Long id;
    private  ReviewSubjectDto reviewSubject;
    private  Date createdAt;
    private  UserDto owner;
    private  String message;
    private  ReportStatus status;

    /**
     * A DTO for the {@link ReviewSubject} entity
     */
    @Data
    public static class ReviewSubjectDto implements Serializable {
        private  Long id;
        private  String review;
        private  boolean done;
        private  Date createdAt;
        private  boolean isHidden;
        private  ApproveType approved;
        private  boolean isDelete;
    }
}