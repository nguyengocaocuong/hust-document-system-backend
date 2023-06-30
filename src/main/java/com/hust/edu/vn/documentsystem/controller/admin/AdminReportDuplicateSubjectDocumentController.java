package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportDuplicateSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.entity.ReportDuplicateSubjectDocument;
import com.hust.edu.vn.documentsystem.service.ReportDuplicateSubjectDocumentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/reportDuplicateSubjectDocument")
public class AdminReportDuplicateSubjectDocumentController {
    private final ReportDuplicateSubjectDocumentService reportDuplicateSubjectDocumentService;
    private final ModelMapperUtils modelMapperUtils;

    
    public AdminReportDuplicateSubjectDocumentController(ReportDuplicateSubjectDocumentService reportDuplicateSubjectDocumentService, ModelMapperUtils modelMapperUtils) {
        this.reportDuplicateSubjectDocumentService = reportDuplicateSubjectDocumentService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllReportDuplicateSubjectDocument() {
        List<ReportDuplicateSubjectDocument> reportDuplicateSubjectDocuments = reportDuplicateSubjectDocumentService.getAllReportDuplicateSubjectDocument();
        return CustomResponse.generateResponse(HttpStatus.OK, reportDuplicateSubjectDocuments.stream().map(report -> modelMapperUtils.mapAllProperties(report, ReportDuplicateSubjectDocumentDto.class)));
    }
}
