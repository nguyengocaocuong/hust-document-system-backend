package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/teachers")
public class UserTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    
    public UserTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.teacherService = teacherService;
    }

    @GetMapping("allTeacherForFilter")
    public ResponseEntity<CustomResponse> getAllSubjectForFilter() {
        return CustomResponse.generateResponse(HttpStatus.OK, teacherService.getAllTeachersForFilter());
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createTeacher(@ModelAttribute TeacherModel teacherModel) {
        Teacher teacher = teacherService.createTeacher(teacherModel);
        teacher.setSubjects(null);
        return CustomResponse.generateResponse(HttpStatus.OK,
                modelMapperUtils.mapAllProperties(teacher, TeacherDto.class));
    }

    @GetMapping("/reviewTeacher")
    public ResponseEntity<CustomResponse> getAllReviewTeacher() {
        List<ReviewTeacher> reviewTeacherList = teacherService.findAllReviewTeacher();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeacherList.stream().map(review -> {
            review.getTeacher().setSubjects(null);
            return modelMapperUtils.mapAllProperties(review, ReviewTeacherDto.class);
        }));
    }


    @GetMapping("reviewTeacher/owner")
    public ResponseEntity<CustomResponse> getAllReviewTeacherCreatedByUser() {
        List<ReviewTeacher> reviewTeachers = teacherService.getAllReviewTeacherCreatedByUser();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeachers.stream()
                .map(review -> modelMapperUtils.mapAllProperties(review, ReviewTeacherDto.class)));
    }

}
