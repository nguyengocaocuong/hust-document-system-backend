package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/teachers/reviewTeacher")
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

    @GetMapping("new")
    public ResponseEntity<CustomResponse> getAllNewReviewTeacher(){
        List<ReviewTeacher> reviewTeachers = reviewTeacherService.getAllNewReviewTeacher();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeachers.stream().map(review -> modelMapperUtils.mapAllProperties(review, ReviewTeacherDto.class)));
    }

}
