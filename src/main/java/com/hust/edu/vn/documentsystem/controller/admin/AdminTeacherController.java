package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/admins/teachers")
@Tag(name = "Teacher - api")
@Slf4j
public class AdminTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.teacherService = teacherService;
    }
    @GetMapping()
    public ResponseEntity<CustomResponse> getAllTeachers() {
        List<Object> content = teacherService.getAllTeachers().stream().map(teacher -> modelMapperUtils.mapAllProperties(teacher, TeacherDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các giảng viên",content);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<CustomResponse> deleteTeacher(@PathVariable("id") Long teacherId ){
        boolean isDeleted = teacherService.deleteTeacher(teacherId);
        return CustomResponse.generateResponse(isDeleted);
    }

}
