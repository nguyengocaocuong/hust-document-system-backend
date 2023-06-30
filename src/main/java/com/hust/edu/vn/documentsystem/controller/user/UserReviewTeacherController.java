package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/teachers/{teacherId}/reviewTeacher")
public class UserReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;

    public UserReviewTeacherController(ReviewTeacherService reviewTeacherService, ModelMapperUtils modelMapperUtils) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createReviewTeacher(@ModelAttribute ReviewTeacherModel reviewTeacherModel, @PathVariable("teacherId") Long teacherId){
        ReviewTeacher reviewTeacher = reviewTeacherService.createReviewTeacher(teacherId, reviewTeacherModel);
        if(reviewTeacher == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(reviewTeacher, ReviewTeacherDto.class));
    }

    @DeleteMapping("{reviewTeacherId}")
    public ResponseEntity<CustomResponse> deleteReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId, @PathVariable("teacherId") Long teacherId){
        boolean status = reviewTeacherService.deleteReviewTeacher(reviewTeacherId, teacherId);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("{reviewTeacherId}")
    public ResponseEntity<CustomResponse> updateReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId,@PathVariable("teacherId") Long teacherId, @ModelAttribute ReviewTeacherModel reviewTeacherModel){
        boolean status = reviewTeacherService.updateReviewTeacher(reviewTeacherId, teacherId, reviewTeacherModel);
        return CustomResponse.generateResponse(status);
    }

}
