package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import com.spire.ms.System.Collections.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/admins/teachers")
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
        List<Object[]> results = teacherService.getAllTeacherForAdmin();
        List<Map<String, Object>> resultMap = new ArrayList();
        results.forEach(result ->{
            Map<String, Object> map = new HashMap<>();
            Teacher teacher = (Teacher) result[0];
            map.put("teacher", modelMapperUtils.mapAllProperties(teacher, TeacherDto.class));
            map.put("reviewTeacherTotal", result[1]);
            resultMap.add(map);
        });
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các giảng viên",resultMap);
    }

    @PatchMapping()
    public ResponseEntity<CustomResponse> updateTeacher(@ModelAttribute TeacherModel teacherModel){
        boolean status = teacherService.updateTeacher(teacherModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("{teacherId}")
    public ResponseEntity<CustomResponse> deleteTeacher(@PathVariable("teacherId") Long teacherId){
        boolean status = teacherService.deleteTeacher(teacherId);
        return CustomResponse.generateResponse(status);
    }
}
