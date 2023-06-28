package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.service.SubjectDocumentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}/favorite")
public class FavoriteSubjectDocumentController {
    private final SubjectDocumentService subjectDocumentService;
    private final ModelMapperUtils modelMapperUtils;

    public FavoriteSubjectDocumentController(SubjectDocumentService subjectDocumentService, ModelMapperUtils modelMapperUtils) {
        this.subjectDocumentService = subjectDocumentService;
        this.modelMapperUtils = modelMapperUtils;
    }
}
