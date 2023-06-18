package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportContentReviewSubjectDto;
import com.hust.edu.vn.documentsystem.entity.ReportContentReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReportContentReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/reportContentReviewSubject")
public class ReportContentReviewSubjectController {
    private final ReportContentReviewSubjectService reportContentReviewSubjectService;
private final ModelMapperUtils mapperUtils;
    @Autowired
    public ReportContentReviewSubjectController(ReportContentReviewSubjectService reportContentReviewSubjectService, ModelMapperUtils mapperUtils) {
        this.reportContentReviewSubjectService = reportContentReviewSubjectService;
        this.mapperUtils = mapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllReportContentForReviewSubject(){
        List<ReportContentReviewSubject> reportContentReviewSubjects = reportContentReviewSubjectService.getAllReportContentForReviewSubject();
        return CustomResponse.generateResponse(HttpStatus.OK, reportContentReviewSubjects.stream().map(report -> mapperUtils.mapAllProperties(report, ReportContentReviewSubjectDto.class)));
    }
}
