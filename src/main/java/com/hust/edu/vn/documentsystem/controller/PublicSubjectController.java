package com.hust.edu.vn.documentsystem.controller;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.service.SubjectService;
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
@RequestMapping("/api/v1/public/subjects")
public class PublicSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public PublicSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.subjectService = subjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllTeacher(){
        List<Subject> subjects = subjectService.getAllSubjects();
        List<Object> subjectDto = subjects.stream().map(teacher -> {teacher.setTeachers(null);teacher.setOwner(null); return modelMapperUtils.mapAllProperties(teacher, SubjectDto.class);}).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDto);

    }
}

