package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewSubjectDto;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.PropertyMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins/subjects/reviewSubject")
@Tag(name="Review subject - api")
public class AdminReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public AdminReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllReviewSubject() {
        PropertyMap<ReviewSubject, ReviewSubjectDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getSubject());
                skip(destination.getOwner());
            }
        };
        Object content = reviewSubjectService.getAllReviewSubjects().stream().map(reviewSubject -> modelMapperUtils.mapSelectedProperties(reviewSubject, ReviewSubjectDto.class, propertyMap));
        return CustomResponse.generateResponse(HttpStatus.OK, content);
    }
    @PatchMapping("hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenReviewSubject(@PathVariable("id") Long id) {
        boolean status = reviewSubjectService.hiddenReviewSubject(id);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("active/{id}")
    public ResponseEntity<CustomResponse> activeReviewSubject(@PathVariable("id") Long id) {
        boolean status = reviewSubjectService.activeReviewSubject(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("approve/{id}")
    public ResponseEntity<CustomResponse> approvedReviewSubject(@PathVariable("id") Long id) {
        boolean status = reviewSubjectService.approvedReviewSubject(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("unApprove/{id}")
    public ResponseEntity<CustomResponse> unApprovedReviewSubject(@PathVariable("id") Long id) {
        boolean status = reviewSubjectService.unApprovedReviewSubject(id);
        return CustomResponse.generateResponse(status);
    }
}
