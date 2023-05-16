package com.hust.edu.vn.documentsystem.controller;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/teachers")
public class PublicTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public PublicTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.teacherService = teacherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllTeacher(){
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Object> teacherDtos = teachers.stream().map(teacher -> {teacher.setSubjects(null);teacher.setOwner(null); return modelMapperUtils.mapAllProperties(teacher, TeacherDto.class);}).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, teacherDtos);

    }
}
