package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins/subjects")
@Tag(name = "Subjects - api")
public class AdminSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.subjectService = subjectService;
    }


    @GetMapping()
    public ResponseEntity<CustomResponse> getAllSubjects() {
        Object content = subjectService.getAllSubjects().stream().map(subject -> modelMapperUtils.mapAllProperties(subject, SubjectDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các môn học có trong hệ thống", content);
    }
    @DeleteMapping("subjects/{id}")
    public ResponseEntity<CustomResponse> deleteSubject(@PathVariable("id") Long subjectId){
        boolean status = subjectService.deleteSubject(subjectId);
        return CustomResponse.generateResponse(status);
    }
}
