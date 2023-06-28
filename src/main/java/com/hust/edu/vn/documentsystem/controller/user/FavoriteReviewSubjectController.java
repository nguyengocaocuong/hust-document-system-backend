package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/subjects/reviewSubject/{reviewSubjectId}/favorite")
public class FavoriteReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public FavoriteReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }
}
