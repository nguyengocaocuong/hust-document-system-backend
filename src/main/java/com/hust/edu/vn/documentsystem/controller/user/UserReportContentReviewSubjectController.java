package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReportContentReviewSubjectDto;
import com.hust.edu.vn.documentsystem.data.model.ReportContentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.data.model.ReportContentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.ReportContentReviewSubject;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/subjects/reviewSubject/{reviewSubjectId}/reportContent")
public class UserReportContentReviewSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserReportContentReviewSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.subjectService = subjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> reportContentReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId, @ModelAttribute ReportContentReviewSubjectModel reportContentReviewSubjectModel){
        ReportContentReviewSubject reportContentReviewSubject = subjectService.createReportContentReviewSubject(reviewSubjectId, reportContentReviewSubjectModel);
        if(reportContentReviewSubject == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.CREATED, modelMapperUtils.mapAllProperties(reportContentReviewSubject, ReportContentReviewSubjectDto.class));
    }
    @PatchMapping("{reportContentReviewSubjectId}")
    public ResponseEntity<CustomResponse> updateReportContentReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId, @PathVariable("reportContentReviewSubjectId") Long reportContentReviewSubjectId,@ModelAttribute ReportContentReviewSubjectModel reportContentReviewSubjectModel){
        return CustomResponse.generateResponse(subjectService.updateReportContentReviewSubject(reviewSubjectId,reportContentReviewSubjectId, reportContentReviewSubjectModel));
    }
    @DeleteMapping("{reportContentReviewSubjectId}")
    public ResponseEntity<CustomResponse> deleteReportContentReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId, @PathVariable("reportContentReviewSubjectId") Long reportContentReviewSubjectId){
        return CustomResponse.generateResponse(subjectService.deleteReportContentReviewSubject(reviewSubjectId,reportContentReviewSubjectId));

    }
}
