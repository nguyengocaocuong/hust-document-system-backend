package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/teachers/{teacherId}/reviewTeacher")
public class ReviewTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    public ReviewTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.teacherService = teacherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createReviewTeacher(@ModelAttribute ReviewTeacherModel reviewTeacherModel, @PathVariable("teacherId") Long teacherId){
        ReviewTeacher reviewTeacher = teacherService.createReviewTeacher(teacherId, reviewTeacherModel);
        if(reviewTeacher == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(reviewTeacher, ReviewTeacherDto.class));
    }
}
