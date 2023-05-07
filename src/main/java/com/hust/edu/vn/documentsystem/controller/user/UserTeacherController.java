package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/users/teachers")
@Tag(name = "Teachers - api")
@Slf4j
public class UserTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public UserTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.teacherService = teacherService;
    }

    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllTeachersCreateByUser(){
        Object content = teacherService.getAllTeachersCreateByUser().stream().map(teacher -> modelMapperUtils.mapAllProperties(teacher, TeacherDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các giảng viên",content);
    }

}
