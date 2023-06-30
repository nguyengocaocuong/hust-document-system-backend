package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewSubjectDto;
import com.hust.edu.vn.documentsystem.data.model.ReviewSubjectModel;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/subjects/{subjectId}/reviewSubject")
public class UserReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createReviewSubject(@PathVariable("subjectId") Long subjectId, @ModelAttribute ReviewSubjectModel reviewSubjectModel){
        ReviewSubject reviewSubject = reviewSubjectService.createReviewSubject(subjectId, reviewSubjectModel);
        if(reviewSubject == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(reviewSubject, ReviewSubjectDto.class));
    }
    @DeleteMapping("{reviewSubjectId}")
    public ResponseEntity<CustomResponse> deleteReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId, @PathVariable("subjectId") Long subjectId ) {
        boolean status = reviewSubjectService.deleteReviewSubject(reviewSubjectId, subjectId);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("{reviewSubjectId}")
    public ResponseEntity<CustomResponse> updateReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId, @PathVariable("subjectId") Long subjectId, @ModelAttribute ReviewSubjectModel reviewSubjectModel){
        boolean status = reviewSubjectService.updateReviewSubject(reviewSubjectId,subjectId , reviewSubjectModel);
        return CustomResponse.generateResponse(status);
    }
}
