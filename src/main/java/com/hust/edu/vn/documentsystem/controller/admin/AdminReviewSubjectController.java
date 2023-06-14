package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewSubjectDto;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.modelmapper.PropertyMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/subjects/reviewSubject")
public class AdminReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public AdminReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping("new")
    public ResponseEntity<CustomResponse> getAllNewReviewSubject(){
        List<ReviewSubject> reviewSubjects = reviewSubjectService.getAllNewReviewSubject();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjects.stream().map(review -> {
            review.getSubject().setSubjectDocuments(null);
            review.setComments(null);
            review.setFavorites(null);
            review.getSubject().setTeachers(null);
            review.getSubject().setOwner(null);
            return modelMapperUtils.mapAllProperties(review, ReviewSubjectDto.class);
        }));
    }
}
