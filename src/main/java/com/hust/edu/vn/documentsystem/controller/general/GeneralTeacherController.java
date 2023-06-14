package com.hust.edu.vn.documentsystem.controller.general;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/generals/teachers")
@Slf4j
public class GeneralTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public GeneralTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.teacherService = teacherService;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<CustomResponse> getTeacher(@PathVariable("id") Long id) {
        TeacherDto teacher = modelMapperUtils.mapAllProperties(teacherService.getTeacherById(id), TeacherDto.class);
        return CustomResponse.generateResponse(HttpStatus.OK, "Thông tin giảng viên", teacher);
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createTeacher(@ModelAttribute TeacherModel teacherModel) {
        Teacher teacher = teacherService.createTeacher(teacherModel);
        return CustomResponse.generateResponse(teacher != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, teacher);
    }


    @PostMapping("subjects")
    public ResponseEntity<CustomResponse> addSubjects(@ModelAttribute TeacherModel teacherModel){
        boolean status = teacherService.addSubjects(teacherModel);
        return CustomResponse.generateResponse(status);
    }

}
