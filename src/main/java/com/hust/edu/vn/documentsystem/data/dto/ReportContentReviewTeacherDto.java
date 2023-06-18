package com.hust.edu.vn.documentsystem.data.dto;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReportContentReviewTeacher} entity
 */
@Data
public class ReportContentReviewTeacherDto implements Serializable {
    private Long id;
    private ReviewTeacherDto1 reviewTeacher;
    private Date createdAt;
    private UserDto owner;
    private String message;
    private String processMessage;
    private ReportStatus status;

    /**
     * A DTO for the {@link com.hust.edu.vn.documentsystem.entity.ReviewTeacher} entity
     */
    @Data
    public static class ReviewTeacherDto1 implements Serializable {
        private Long id;
        private String review;
        private boolean done;
        private Date createdAt;
        private boolean hidden;
        private ApproveType approved;
        private boolean isDelete;
    }
}