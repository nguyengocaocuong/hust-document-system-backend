package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.model.ReportContentModel;
import com.hust.edu.vn.documentsystem.data.model.ReportDuplicateModel;
import com.hust.edu.vn.documentsystem.entity.ReportContent;
import com.hust.edu.vn.documentsystem.entity.ReportDuplicateDocument;
import com.hust.edu.vn.documentsystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/reports")
public class UserReportController {
    private final ReportService reportService;

    @Autowired
    public UserReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("content")
    public ResponseEntity<CustomResponse> reportContent(@ModelAttribute ReportContentModel reportContentModel) {
        ReportContent reportContent = reportService.reportContent(reportContentModel);
        return CustomResponse.generateResponse(reportContent != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, reportContent);
    }

    @PostMapping("duplicate")
    public ResponseEntity<CustomResponse> reportDuplicate(@ModelAttribute ReportDuplicateModel reportDuplicateModel) {
        ReportDuplicateDocument reportDuplicateDocument = reportService.reportDuplicate(reportDuplicateModel);
        return CustomResponse.generateResponse(reportDuplicateDocument != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, reportDuplicateDocument);
    }
}
