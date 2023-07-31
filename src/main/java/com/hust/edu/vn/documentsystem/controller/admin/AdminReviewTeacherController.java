package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admins/teachers/reviewTeacher")
@Slf4j
public class AdminReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;
    private final PusherService pusherService;

    
    public AdminReviewTeacherController(
            ReviewTeacherService reviewTeacherService,
            ModelMapperUtils modelMapperUtils,
            PusherService pusherService) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
        this.pusherService = pusherService;
    }

    @GetMapping("new")
    public ResponseEntity<CustomResponse> getAllNewReviewTeacher(){
        List<ReviewTeacher> reviewTeachers = reviewTeacherService.getAllNewReviewTeacher();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeachers.stream().map(review -> modelMapperUtils.mapAllProperties(review, ReviewTeacherDto.class)));
    }

    @PatchMapping("{reviewTeacherId}/approve")
    public ResponseEntity<CustomResponse> approveReviewSubject(@PathVariable("reviewTeacherId") Long reviewTeacherId){
        ReviewTeacher reviewTeacher = reviewTeacherService.approveReviewTeacher(reviewTeacherId);
        if(reviewTeacher == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        reviewTeacher.setReportContents(null);
        reviewTeacher.setFavorites(null);
        reviewTeacher.setComments(null);
        ReviewTeacherDto reviewTeacherDto = modelMapperUtils.mapAllProperties(reviewTeacher, ReviewTeacherDto.class);
        pusherService.triggerChanel("notification" , "review-teacher-" + reviewTeacher.getOwner().getId(), reviewTeacherDto);
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeacherDto);
    }

    @PatchMapping("{reviewTeacherId}/reject")
    public ResponseEntity<CustomResponse> rejectReviewSubject(@PathVariable("reviewTeacherId") Long reviewTeacherId){
        ReviewTeacher reviewTeacher = reviewTeacherService.rejectReviewTeacher(reviewTeacherId);
        if(reviewTeacher == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        reviewTeacher.setReportContents(null);
        reviewTeacher.setFavorites(null);
        reviewTeacher.setComments(null);
        ReviewTeacherDto reviewTeacherDto = modelMapperUtils.mapAllProperties(reviewTeacher, ReviewTeacherDto.class);
        pusherService.triggerChanel("notification" , "review-teacher-" + reviewTeacher.getOwner().getId(), reviewTeacherDto);
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeacherDto);
    }

}
