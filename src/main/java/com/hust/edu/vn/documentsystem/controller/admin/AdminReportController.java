package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportContentDto;
import com.hust.edu.vn.documentsystem.data.dto.ReportDuplicateDocumentDto;
import com.hust.edu.vn.documentsystem.data.model.ProcessDuplicateModel;
import com.hust.edu.vn.documentsystem.data.model.ProcessReportContentModel;
import com.hust.edu.vn.documentsystem.entity.ReportContent;
import com.hust.edu.vn.documentsystem.entity.ReportDuplicateDocument;
import com.hust.edu.vn.documentsystem.service.ReportService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/reports")
@Tag(name = "Reports - api")
public class AdminReportController {
    private final ReportService reportService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminReportController(
            ReportService reportService,
            ModelMapperUtils modelMapperUtils
    ) {
        this.reportService = reportService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllReports(){
        List<ReportContent> reportContents = reportService.getAllReportContents();
        List<ReportDuplicateDocument> reportDuplicateDocuments = reportService.getReportDuplicateDocuments();
        List<Object> content = new ArrayList<>();
        content.add(reportContents.stream().map(reportContent -> modelMapperUtils.mapAllProperties(reportContent, ReportContentDto.class)).toList());
        content.add(reportDuplicateDocuments.stream().map(reportDuplicateDocument -> modelMapperUtils.mapAllProperties(reportDuplicateDocument, ReportDuplicateDocumentDto.class)).toList());
        return CustomResponse.generateResponse(HttpStatus.OK, content);
    }
    @PatchMapping("content")
    public ResponseEntity<CustomResponse> processReportContent(ProcessReportContentModel processReportContentModel) {
        boolean isReport = reportService.processReportContent(processReportContentModel);
        return CustomResponse.generateResponse(isReport);
    }

    @PatchMapping("duplicate")
    public ResponseEntity<CustomResponse> processReportDuplicate(ProcessDuplicateModel processDuplicateModel) {
        boolean isReport = reportService.processReportDuplicate(processDuplicateModel);
        return CustomResponse.generateResponse(isReport);
    }
}
