package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportContentSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.entity.ReportContentSubjectDocument;
import com.hust.edu.vn.documentsystem.service.ReportContentSubjectDocumentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/reportContentSubjectDocument")
public class AdminReportContentSubjectDocumentController {
    private final ReportContentSubjectDocumentService reportContentSubjectDocumentService;
    private final ModelMapperUtils modelMapperUtils;

    
    public AdminReportContentSubjectDocumentController(ReportContentSubjectDocumentService reportContentSubjectDocumentService, ModelMapperUtils modelMapperUtils) {
        this.reportContentSubjectDocumentService = reportContentSubjectDocumentService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllReportContentSubjectDocument(){
        List<ReportContentSubjectDocument> reportContentSubjectDocuments = reportContentSubjectDocumentService.getAllReportContentSubjectDocument();
        return CustomResponse.generateResponse(HttpStatus.OK, reportContentSubjectDocuments.stream().map(report-> modelMapperUtils.mapAllProperties(report, ReportContentSubjectDocumentDto.class)));
    }
}
