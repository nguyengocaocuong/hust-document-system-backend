package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewSubjectDto;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admins/subjects/reviewSubject")
public class AdminReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    private final PusherService pusherService;

    public AdminReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils, PusherService pusherService) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
        this.pusherService = pusherService;
    }

    @GetMapping("new")
    public ResponseEntity<CustomResponse> getAllNewReviewSubject() {
        List<ReviewSubject> reviewSubjects = reviewSubjectService.getAllNewReviewSubject();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjects.stream().map(review -> {
            review.getSubject().setSubjectDocuments(null);
            review.setComments(null);
            review.setFavorites(null);
            review.getSubject().setOwner(null);
            return modelMapperUtils.mapAllProperties(review, ReviewSubjectDto.class);
        }));
    }

    @PatchMapping("{reviewSubjectId}/approve")
    public ResponseEntity<CustomResponse> approveReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId) {
        ReviewSubject reviewSubject = reviewSubjectService.approveReviewSubject(reviewSubjectId);
        if (reviewSubject == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        Map<String, Object> notification = new HashMap();
        notification.put("type", "APPROVE");
        notification.put("approveType", "REVIEW_SUBJECT");
        notification.put("approved", reviewSubject.getApproved());
        notification.put("name", reviewSubject.getSubject().getName());
        ReviewSubjectDto reviewSubjectDto = modelMapperUtils.mapAllProperties(reviewSubject, ReviewSubjectDto.class);
        pusherService.triggerChanel("notification", "review-subject-" + reviewSubject.getOwner().getId(), notification);
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjectDto);
    }

    @PatchMapping("{reviewSubjectId}/reject")
    public ResponseEntity<CustomResponse> rejectReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId) {
        ReviewSubject reviewSubject = reviewSubjectService.rejectReviewSubject(reviewSubjectId);
        if (reviewSubject == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        Map<String, Object> notification = new HashMap();
        notification.put("type", "APPROVE");
        notification.put("approveType", "REVIEW_SUBJECT");
        notification.put("approved", reviewSubject.getApproved());
        notification.put("name", reviewSubject.getSubject().getName());
        ReviewSubjectDto reviewSubjectDto = modelMapperUtils.mapAllProperties(reviewSubject, ReviewSubjectDto.class);
        pusherService.triggerChanel("notification", "review-subject-" + reviewSubject.getOwner().getId(), notification);
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjectDto);
    }
}
