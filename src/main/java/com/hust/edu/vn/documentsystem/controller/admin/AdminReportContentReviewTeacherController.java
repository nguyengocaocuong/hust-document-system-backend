package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportContentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.entity.ReportContentReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReportContentReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/reportContentReviewTeacher")
public class AdminReportContentReviewTeacherController {
    private final ReportContentReviewTeacherService reportContentReviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminReportContentReviewTeacherController(ReportContentReviewTeacherService reportContentReviewTeacherService, ModelMapperUtils modelMapperUtils) {
        this.reportContentReviewTeacherService = reportContentReviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllReportContentForReviewTeacher(){
        List<ReportContentReviewTeacher> reportContentReviewTeachers = reportContentReviewTeacherService.getAllReportContentForReviewSubject();
        return CustomResponse.generateResponse(HttpStatus.OK, reportContentReviewTeachers.stream().map(report -> modelMapperUtils.mapAllProperties(report, ReportContentReviewTeacherDto.class)));
    }
}
