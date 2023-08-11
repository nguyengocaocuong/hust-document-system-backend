package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDocumentTypeDto;
import com.hust.edu.vn.documentsystem.data.model.SubjectDocumentTypeModel;
import com.hust.edu.vn.documentsystem.entity.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentTypeService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins/subjectDocumentType")
public class AdminSubjectDocumentTypeController {
    private final SubjectDocumentTypeService subjectDocumentTypeService;

    private final ModelMapperUtils modelMapperUtils;
    public AdminSubjectDocumentTypeController(SubjectDocumentTypeService subjectDocumentTypeService, ModelMapperUtils modelMapperUtils) {
        this.subjectDocumentTypeService = subjectDocumentTypeService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createSubjectDocumentType(@ModelAttribute SubjectDocumentTypeModel subjectDocumentTypeModel){
        SubjectDocumentType subjectDocumentType = subjectDocumentTypeService.createSubjectDocumentType(subjectDocumentTypeModel);
        if(subjectDocumentType == null) return CustomResponse.generateResponse(HttpStatus.CONFLICT);
        return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(subjectDocumentType, SubjectDocumentTypeDto.class));
    }
}
