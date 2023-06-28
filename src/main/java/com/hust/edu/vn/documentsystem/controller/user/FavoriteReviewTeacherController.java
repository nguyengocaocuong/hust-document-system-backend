package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/teachers/reviewTeacher/{reviewTeacherId}/favorite")
public class FavoriteReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;

    public FavoriteReviewTeacherController(ReviewTeacherService reviewTeacherService, ModelMapperUtils modelMapperUtils) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }
}
