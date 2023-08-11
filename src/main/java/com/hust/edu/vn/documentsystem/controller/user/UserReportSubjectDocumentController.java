package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportContentSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.data.model.ReportContentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.data.model.ReportContentSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportDuplicateSubjectDocumentModel;
import com.hust.edu.vn.documentsystem.entity.ReportContentSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.ReportDuplicateSubjectDocument;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}")
public class UserReportSubjectDocumentController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserReportSubjectDocumentController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.subjectService = subjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping("reportContent")
    public ResponseEntity<CustomResponse> reportContentSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @ModelAttribute ReportContentSubjectDocumentModel reportContentSubjectDocumentModel){
        ReportContentSubjectDocument reportContentSubjectDocument = subjectService.createReportContentSubjectDocument(subjectDocumentId, reportContentSubjectDocumentModel);
        if(reportContentSubjectDocument == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(reportContentSubjectDocument, ReportContentSubjectDocumentDto.class));
    }
    @PostMapping("reportDuplicate")
    public ResponseEntity<CustomResponse> reportDuplicateSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @ModelAttribute ReportDuplicateSubjectDocumentModel reportContentSubjectDocumentModel){
        ReportDuplicateSubjectDocument reportDuplicateSubjectDocument = subjectService.createReportDuplicateSubjectDocument(subjectDocumentId, reportContentSubjectDocumentModel);
        if(reportDuplicateSubjectDocument == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(reportDuplicateSubjectDocument, ReportContentSubjectDocumentDto.class));
    }

    @PatchMapping("reportContent/{reportContentSubjectDocumentId}")
    public ResponseEntity<CustomResponse> updateReportContentSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("reportContentSubjectDocumentId") Long reportContentSubjectDocumentId,@ModelAttribute ReportContentSubjectDocumentModel reportContentSubjectDocumentModel){
        return CustomResponse.generateResponse(subjectService.updateReportContentSubjectDocument(subjectDocumentId,reportContentSubjectDocumentId, reportContentSubjectDocumentModel));
    }
    @DeleteMapping("reportContent/{reportContentSubjectDocumentId}")
    public ResponseEntity<CustomResponse> deleteReportContentSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @PathVariable("reportContentSubjectDocumentId") Long reportContentSubjectDocumentId){
        return CustomResponse.generateResponse(subjectService.deleteReportContentSubjectDocument(subjectDocumentId,reportContentSubjectDocumentId));
    }
}
