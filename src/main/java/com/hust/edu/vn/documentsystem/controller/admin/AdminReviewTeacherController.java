package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins/teachers/reviewTeacher")
@Tag(name = "Review teacher - api")
public class AdminReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminReviewTeacherController(
            ReviewTeacherService reviewTeacherService,
            ModelMapperUtils modelMapperUtils
    ) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllReviewTeacher() {
        PropertyMap<ReviewTeacher, ReviewTeacherDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getTeacher());
                skip(destination.getOwner());
            }
        };
        Object content = reviewTeacherService.getAllReviewTeachers().stream().map(reviewTeacher -> modelMapperUtils.mapSelectedProperties(reviewTeacher, ReviewTeacherDto.class, propertyMap));
        return CustomResponse.generateResponse(HttpStatus.OK, content);
    }

    @PatchMapping("hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenReviewTeacher(@PathVariable("id") Long id) {
        boolean status = reviewTeacherService.hiddenReviewTeacher(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("active/{id}")
    public ResponseEntity<CustomResponse> activeReviewTeacher(@PathVariable("id") Long id) {
        boolean status = reviewTeacherService.activeReviewTeacher(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("approve/{id}")
    public ResponseEntity<CustomResponse> approvedReviewTeacher(@PathVariable("id") Long id) {
        boolean status = reviewTeacherService.approvedReviewTeacher(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("unApprove/{id}")
    public ResponseEntity<CustomResponse> unApprovedReviewTeacher(@PathVariable("id") Long id) {
        boolean status = reviewTeacherService.unApprovedReviewTeacher(id);
        return CustomResponse.generateResponse(status);
    }
}
