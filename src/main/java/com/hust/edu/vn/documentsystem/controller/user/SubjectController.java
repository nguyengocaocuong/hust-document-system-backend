package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.*;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/subjects")
@Slf4j
public class SubjectController {
    private final SubjectService subjectService;
    private final ModelMapperUtils modelMapperUtils;
    private final ReviewSubjectService reviewSubjectService;

    @Autowired
    public SubjectController(SubjectService subjectService, ModelMapperUtils modelMapperUtils, ReviewSubjectService reviewSubjectService
    ) {
        this.modelMapperUtils = modelMapperUtils;
        this.subjectService = subjectService;
        this.reviewSubjectService = reviewSubjectService;
    }

    //TODO: giữ lại các api ở đây
    @GetMapping("{subjectId}")
    public ResponseEntity<CustomResponse> getSubjectDetail(@PathVariable Long subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        return CustomResponse.generateResponse(HttpStatus.OK, "Thông tin chi tiết môn học", modelMapperUtils.mapAllProperties(subject, SubjectDto.class));
    }

    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllSubjectsCreateByUser() {
        Object content = subjectService.getAllSubjectsCreateByUser().stream().map(subject -> modelMapperUtils.mapAllProperties(subject, SubjectDto.class)).toList();
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
    public ResponseEntity<CustomResponse> uploadDocumentForSubject(@ModelAttribute SubjectDocumentModel subjectDocumentModel, @PathVariable("subjectId") Long subjectId) {
        SubjectDocument subjectDocument = subjectService.saveDocumentForSubject(subjectDocumentModel, subjectId);
        subjectDocument.setSubject(null);
        subjectDocument.setOwner(null);
        return CustomResponse.generateResponse(HttpStatus.OK, subjectDocument);
    }

    @PostMapping("subjectDocument/{subjectDocumentId}/answerSubjectDocument")
    public ResponseEntity<CustomResponse> uploadAnswerForSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @ModelAttribute AnswerSubjectDocumentModel answerSubjectDocumentModel) {
        AnswerSubjectDocument answerSubjectDocument = subjectService.saveAnswerForSubjectDocument(subjectDocumentId, answerSubjectDocumentModel);
        answerSubjectDocument.setSubjectDocument(null);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(answerSubjectDocument, AnswerSubjectDocumentDto.class));
    }

    @GetMapping("subjectDocument/{subjectDocumentId}/answerSubjectDocument")
    public ResponseEntity<CustomResponse> getAllAnswerSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        List<AnswerSubjectDocument> answerSubjectDocuments = subjectService.getAllAnswerSubjectDocument(subjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK, answerSubjectDocuments.stream().map(answer -> modelMapperUtils.mapAllProperties(answer, AnswerSubjectDocumentDto.class)));
    }

    @PostMapping("answerSubjectDocument/{answerSubjectDocumentId}/favorite")
    public ResponseEntity<CustomResponse> toggleFavoriteSubjectDocument(@PathVariable("answerSubjectDocumentId") Long answerSubjectDocumentID) {
        boolean status = subjectService.favoriteAnswerSubjectDocument(answerSubjectDocumentID);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("answerSubjectDocument/{answerSubjectDocumentId}/favorite")
    public ResponseEntity<CustomResponse> getAllFavoriteForAnswerSubjectDocument(@PathVariable("answerSubjectDocumentId") Long answerSubjectDocumentId) {
        List<FavoriteAnswerSubjectDocument> answerSubjectDocuments = subjectService.getAllFavoriteAnswerSubjectDocument(answerSubjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK, answerSubjectDocuments.stream().map(favorite -> modelMapperUtils.mapAllProperties(favorite, FavoriteAnswerSubjectDocumentDto.class)));
    }

    @GetMapping("subjectDocument/{subjectDocumentId}/readFile")
    public ResponseEntity<Resource> readSubjectDocumentFile(@PathVariable("subjectDocumentId") Long subjectDocumentId, @RequestParam(required = false) String token) {
        List<Object> data = subjectService.readSubjectDocumentFile(subjectDocumentId, token);
        if (data == null || ((byte[]) data.get(1)).length == 0) return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }

    @GetMapping("subjectDocument/{subjectDocumentId}/shared")
    public ResponseEntity<CustomResponse> getSharedUser(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        List<User> users = subjectService.getAllUserShared(subjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK, users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)));
    }

    @PostMapping("subjectDocument/{subjectDocumentId}/share")
    public ResponseEntity<CustomResponse> shareSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @ModelAttribute ShareSubjectDocumentModel shareSubjectDocumentModel) {
        Object object = subjectService.shareDocument(subjectDocumentId, shareSubjectDocumentModel);
        if (object == null) return CustomResponse.generateResponse(HttpStatus.CONFLICT);
        return CustomResponse.generateResponse(HttpStatus.OK, object);
    }

    @GetMapping("subjectDocument/{subjectDocumentId}")
    public ResponseEntity<CustomResponse> getSubjectDocumentDetail(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        SubjectDocument subjectDocument = subjectService.getSubjectDocumentDetailById(subjectDocumentId);
        subjectDocument.getSubject().setSubjectDocuments(null);
        return CustomResponse.generateResponse(subjectDocument != null ? HttpStatus.OK : HttpStatus.NOT_FOUND, subjectDocument != null ? modelMapperUtils.mapAllProperties(subjectDocument, SubjectDocumentDto.class) : null);
    }


    @PostMapping("{subjectId}/favorite")
    public ResponseEntity<CustomResponse> favoriteSubject(@PathVariable("subjectId") Long subjectId, @ModelAttribute FavoriteSubjectModel favoriteSubjectModel) {
        boolean status = subjectService.favoriteSubject(subjectId, favoriteSubjectModel);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("favorite")
    public ResponseEntity<CustomResponse> getFavoriteSubjects() {
        List<FavoriteSubject> content = subjectService.getFavoriteSubjects();
        List<Subject> subjects = content.stream().map(FavoriteSubject::getSubject).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách các môn học mà bạn yêu thích", subjects.stream().map(subject -> {
            subject.setOwner(null);
            return modelMapperUtils.mapAllProperties(subject, SubjectDto.class);
        }).toList());
    }

    @DeleteMapping("favorite/{id}")
    public ResponseEntity<CustomResponse> unFavoriteSubject(@PathVariable("id") Long subjectId) {
        boolean status = subjectService.unFavoriteSubject(subjectId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("favorite")
    public ResponseEntity<CustomResponse> updateFavoriteSubject(@ModelAttribute FavoriteSubjectModel favoriteSubjectModel) {
        boolean status = subjectService.updateFavoriteSubject(favoriteSubjectModel);
        return CustomResponse.generateResponse(status);
    }


    @PostMapping("subjectDocument/{subjectDocumentId}/favorite")
    public ResponseEntity<CustomResponse> favoriteSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        return CustomResponse.generateResponse(subjectService.favoriteSubjectDocument(subjectDocumentId));
    }


    @GetMapping("allUserShareWithMe")
    public ResponseEntity<CustomResponse> getAllUserShareWithMe() {
        List<User> users = subjectService.getAllUserShareWithMe();
        return CustomResponse.generateResponse(HttpStatus.OK, users.stream().map(user -> modelMapperUtils.mapAllProperties(user, UserDto.class)).toList());
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllSubjects() {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllSubjects());
    }


    @GetMapping("allSubjectForFilter")
    public ResponseEntity<CustomResponse> getAllSubjectForFilter() {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.getAllSubjectsForFilter());
    }

    @GetMapping("allSemesterForFilter")
    public ResponseEntity<CustomResponse> getAllSemesterForFilter() {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.findAllSemesterForFilter());
    }

    @GetMapping("subjectDocumentTypeForFilter")
    public ResponseEntity<CustomResponse> getAllSubjectDocumentType() {
        return CustomResponse.generateResponse(HttpStatus.OK, subjectService.findAllSubjectDocumentType());
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createSubject(@ModelAttribute SubjectModel subjectModel) {
        Subject subject = subjectService.createSubject(subjectModel);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(subject, SubjectDto.class));
    }

    @GetMapping("reviewSubject")
    public ResponseEntity<CustomResponse> allReviewSubject() {
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjectService.getAllReviewSubjects().stream().map(review -> modelMapperUtils.mapAllProperties(review, ReviewSubjectDto.class)));
    }


    @PostMapping("reviewSubject/{reviewSubjectId}/favorite")
    public ResponseEntity<CustomResponse> favoriteReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId) {
        boolean status = reviewSubjectService.toggleFavoriteReviewSubject(reviewSubjectId);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("reviewSubject/{reviewSubjectId}/favorite")
    public ResponseEntity<CustomResponse> getAllFavoriteForReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId) {
        List<FavoriteReviewSubject> favoriteForReviewSubject = reviewSubjectService.getAllFavoriteForReviewSubject(reviewSubjectId);
        return CustomResponse.generateResponse(HttpStatus.OK, favoriteForReviewSubject.stream().map(favoriteReviewSubject -> modelMapperUtils.mapAllProperties(favoriteReviewSubject, FavoriteReviewSubjectDto.class)));
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
            share.getSubjectDocument().getSubject().setTeachers(null);
            share.getSubjectDocument().getSubject().setOwner(null);

            return modelMapperUtils.mapAllProperties(share, SharePrivateAllDto.class);
        }));
    }

    @DeleteMapping("subjectDocument/shared/{sharedId}")
    public ResponseEntity<CustomResponse> clearSharedSubjectDocument(@PathVariable("sharedId") Long sharedId) {
        boolean status = subjectService.clearSharedPrivateSubjectDocument(sharedId);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("subjectDocument/{subjectDocumentId}/generatePublicOnInternetUrl")
    public ResponseEntity<CustomResponse> generatePublicOnInternetUrlForSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        String url = subjectService.generatePublicOnInternetUrlForSubjectDocument(subjectDocumentId);
        if (url == null) return CustomResponse.generateResponse(HttpStatus.UNAUTHORIZED);
        return CustomResponse.generateResponse(HttpStatus.OK, url);
    }

    @GetMapping("subjectDocument/{subjectDocumentId}/generatePublicOnWebsiteUrl")
    public ResponseEntity<CustomResponse> generatePublicOnWebsiteUrlForSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        String url = subjectService.generatePublicOnWebsiteUrlForSubjectDocument(subjectDocumentId);
        if (url == null) return CustomResponse.generateResponse(HttpStatus.UNAUTHORIZED);
        return CustomResponse.generateResponse(HttpStatus.OK, url);
    }

    @DeleteMapping("subjectDocument/{subjectDocumentId}/forever")
    public ResponseEntity<CustomResponse> deleteSubjectDocumentForever(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectService.deleteSubjectDocumentForever(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("subjectDocument/{subjectDocumentId}")
    public ResponseEntity<CustomResponse> moveSubjectDocumentToTrash(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectService.moveSubjectDocumentToTrash(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("subjectDocument/{subjectDocumentId}/restore")
    public ResponseEntity<CustomResponse> restoreSubjectDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectService.restoreSubjectDocument(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("subjectDocument/{subjectDocumentId}/public")
    public ResponseEntity<CustomResponse> makeSubjectDocumentPublic(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectService.makeSubjectDocumentPublic(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("subjectDocument/{subjectDocumentId}/private")
    public ResponseEntity<CustomResponse> makeSubjectDocumentPrivate(@PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = subjectService.makeSubjectDocumentPrivate(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("subjectDocument/{subjectDocumentId}/translate")
    public ResponseEntity<Resource> translateDocument(@PathVariable("subjectDocumentId") Long subjectDocumentId, @RequestParam("targetLanguage") TargetLanguageType targetLanguageType) {
        List<Object> data = subjectService.translateSubjectDocument(subjectDocumentId, targetLanguageType);
        if (data == null || ((byte[]) data.get(1)).length == 0) return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }

    @GetMapping("reviewSubject/owner")
    public ResponseEntity<CustomResponse> getAllReviewSubjectCreatedByUser() {
        List<ReviewSubject> reviewSubjects = subjectService.getAllReviewSubjectCreatedByUser();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjects.stream().map(review -> modelMapperUtils.mapAllProperties(review, ReviewSubjectDto.class)));
    }
}
