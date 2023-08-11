package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentTypeDto;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentTypeService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/subjectDocumentType")
public class UserSubjectDocumentTypeController {
    private final SubjectDocumentTypeService subjectDocumentTypeService;
    private final ModelMapperUtils modelMapperUtils;

    public UserSubjectDocumentTypeController(SubjectDocumentTypeService subjectDocumentTypeService, ModelMapperUtils modelMapperUtils) {
        this.subjectDocumentTypeService = subjectDocumentTypeService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping
    public ResponseEntity<CustomResponse> getAllSubjectDocumentTypes(){
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocumentTypeService.getAllSubjectDocumentTypes().stream().map(subjectDocumentType -> modelMapperUtils.mapAllProperties(subjectDocumentType, SubjectDocumentTypeDto.class)));
    }
}
