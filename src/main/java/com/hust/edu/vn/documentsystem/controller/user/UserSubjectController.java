package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.*;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/subjects")
public class UserSubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserSubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.subjectService = subjectService;
    }

    @GetMapping("{subjectId}")
    public ResponseEntity<CustomResponse> getSubjectDetail(@PathVariable Long subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        return CustomResponse.generateResponse(HttpStatus.OK, "Thông tin chi tiết môn học",
                modelMapperUtils.mapAllProperties(subject, SubjectDto.class));
    }

    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllSubjectsCreateByUser() {
        Object content = subjectService.getAllSubjectsCreateByUser().stream()
                .map(subject -> modelMapperUtils.mapAllProperties(subject, SubjectDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các môn học do bạn tạo ", content);
    }

    @PatchMapping()
    public ResponseEntity<CustomResponse> updateSubject(@ModelAttribute SubjectModel subjectModel) {
        boolean status = subjectService.updateSubject(subjectModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CustomResponse> deleteSubject(@PathVariable("id") Long subjectId) {
        boolean isDeleted = subjectService.deleteSubject(subjectId);
        return CustomResponse.generateResponse(isDeleted);
    }

    @PostMapping("{subjectId}/subjectDocument")
    public ResponseEntity<CustomResponse> uploadDocumentForSubject(
            @ModelAttribute SubjectDocumentModel subjectDocumentModel, @PathVariable("subjectId") Long subjectId) {
        SubjectDocument subjectDocument = subjectService.saveDocumentForSubject(subjectDocumentModel, subjectId);
        subjectDocument.setSubject(null);
        subjectDocument.setOwner(null);
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocument);
    }


    @GetMapping("allUserShareWithMe")
    public ResponseEntity<CustomResponse> getAllUserShareWithMe() {
        List<User> users = subjectService.getAllUserShareWithMe();
        return CustomResponse.generateResponse(HttpStatus.OK,
                users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)).toList());
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllSubjects(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllSubjects(page, size));
    }

    @GetMapping("allSubjectForFilter")
    public ResponseEntity<CustomResponse> getAllSubjectForFilter() {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllSubjectsForFilter());
    }


    @GetMapping("subjectDocumentTypeForFilter")
    public ResponseEntity<CustomResponse> getAllSubjectDocumentType() {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.findAllSubjectDocumentType());
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createSubject(@ModelAttribute SubjectModel subjectModel) {
        Subject subject = subjectService.createSubject(subjectModel);
        return CustomResponse.generateResponse(HttpStatus.OK,
                modelMapperUtils.mapAllProperties(subject, SubjectDto.class));
    }

    @GetMapping("reviewSubject")
    public ResponseEntity<CustomResponse> allReviewSubject() {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllReviewSubjects().stream()
                .map(review -> modelMapperUtils.mapAllProperties(review, ReviewSubjectDto.class)));
    }

    @GetMapping("subjectDocument/owner")
    public ResponseEntity<CustomResponse> getAllSubjectDocumentCreateByUser() {
        List<SubjectDocument> subjectDocuments = subjectService.getAllSubjectDocumentCreateByUser();
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocuments.stream().map(document -> {
            document.setAnswers(null);
            document.setComments(null);
            document.setFavorites(null);
            document.setSubject(null);
            return modelMapperUtils.mapAllProperties(document, SubjectDocumentDto.class);
        }));
    }

    @GetMapping("subjectDocument/shared")
    public ResponseEntity<CustomResponse> getAllSubjectDocumentShared() {
        List<SharePrivate> subjectDocuments = subjectService.getAllSubjectDocumentShared();
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocuments.stream().map(share -> {
            share.getSubjectDocument().setFavorites(null);
            share.getSubjectDocument().setComments(null);
            share.getSubjectDocument().setAnswers(null);
            share.getSubjectDocument().setShared(null);
            share.getSubjectDocument().getSubject().setSubjectDocuments(null);
            share.getSubjectDocument().getSubject().setOwner(null);

            return modelMapperUtils.mapAllProperties(share, SharePrivateAllDto.class);
        }));
    }


    @GetMapping("reviewSubject/owner")
    public ResponseEntity<CustomResponse> getAllReviewSubjectCreatedByUser() {
        List<ReviewSubject> reviewSubjects = subjectService.getAllReviewSubjectCreatedByUser();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjects.stream()
                .map(review -> modelMapperUtils.mapAllProperties(review, ReviewSubjectDto.class)));
    }

    @GetMapping("search")
    public ResponseEntity<CustomResponse> getAllSubjectByInstitute(@RequestParam("institute") String institute){
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllSubjectByInstitute(institute).stream().map(subject -> {
           subject.setSubjectDocuments(null);
           subject.setFavorites(null);
           subject.setReviews(null);
            return  modelMapperUtils.mapAllProperties(subject, SubjectDto.class);
        }));
    }
    @GetMapping("institute")
    public ResponseEntity<CustomResponse> getAllSubjectByInstitute(){
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllInstitute());
    }

    @GetMapping("sharedByUser")
    public ResponseEntity<CustomResponse> getAllSharedByUser(@RequestParam("userId") Long userId){
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllSharedByUser(userId).stream().map(subjectDocument -> modelMapperUtils.mapAllProperties(subjectDocument, SubjectDocumentDto.class)));
    }
    @GetMapping("reported")
    public ResponseEntity<CustomResponse> getAllReported(){
        List<Object> queryResults = subjectService.getAllReported();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("reportContentReviewSubjects", ((List<ReportContentReviewSubject>)queryResults.get(0)).stream().map(report -> modelMapperUtils.mapAllProperties(report, ReportContentReviewSubjectDto.class)));
        resultMap.put("reportContentReviewTeachers", ((List<ReportContentReviewTeacher>)queryResults.get(1)).stream().map(report -> modelMapperUtils.mapAllProperties(report, ReportContentReviewTeacherDto.class)));
        resultMap.put("reportContentSubjectDocuments", ((List<ReportContentSubjectDocument>)queryResults.get(2)).stream().map(report -> modelMapperUtils.mapAllProperties(report, ReportContentSubjectDocumentDto.class)));
        resultMap.put("reportDuplicateSubjectDocuments", ((List<ReportDuplicateSubjectDocument>)queryResults.get(3)).stream().map(report -> modelMapperUtils.mapAllProperties(report, ReportDuplicateSubjectDocumentDto.class)));

        return CustomResponse.generateResponse(HttpStatus.OK,resultMap);
    }



}
