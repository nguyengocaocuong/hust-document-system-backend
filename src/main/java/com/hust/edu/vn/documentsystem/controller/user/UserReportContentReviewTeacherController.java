package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportContentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.model.ReportContentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.ReportContentReviewTeacher;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/teachers/reviewTeacher/{reviewTeacherId}/reportContent")
public class UserReportContentReviewTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    public UserReportContentReviewTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.teacherService = teacherService;
        this.modelMapperUtils = modelMapperUtils;
    }
    @PostMapping()
    public ResponseEntity<CustomResponse> reportContentReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId, @ModelAttribute ReportContentReviewTeacherModel reportContentReviewTeacherModel){
        ReportContentReviewTeacher reportContentReviewTeacher = teacherService.createReportContentReviewTeacher(reviewTeacherId, reportContentReviewTeacherModel);
        if(reportContentReviewTeacher == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(reportContentReviewTeacher, ReportContentReviewTeacherDto.class));
    }
    @PatchMapping("{reportContentReviewTeacherId}")
    public ResponseEntity<CustomResponse> updateReportContentReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId, @PathVariable("reportContentReviewTeacherId") Long reportContentReviewTeacherId,@ModelAttribute ReportContentReviewTeacherModel reportContentReviewTeacherModel){
        return CustomResponse.generateResponse(teacherService.updateReportContentReviewTeacher(reviewTeacherId,reportContentReviewTeacherId,reportContentReviewTeacherModel));
    }
    @DeleteMapping("{reportContentReviewTeacherId}")
    public ResponseEntity<CustomResponse> deleteReportContentReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId, @PathVariable("reportContentReviewTeacherId") Long reportContentReviewTeacherId){
        return CustomResponse.generateResponse(teacherService.deleteReportContentReviewTeacher(reviewTeacherId,reportContentReviewTeacherId));

    }
}
