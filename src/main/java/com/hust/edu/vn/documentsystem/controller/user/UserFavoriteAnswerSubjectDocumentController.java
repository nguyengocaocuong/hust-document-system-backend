package com.hust.edu.vn.documentsystem.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.FavoriteAnswerSubjectDocumentDto;
import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.service.FavoriteAnswerSubjectDocumentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/answerSubjectDocument/{answerSubjectDocumentId}/favorite")
public class UserFavoriteAnswerSubjectDocumentController {
    private final FavoriteAnswerSubjectDocumentService favoriteAnswerSubjectDocumentService;
    private final ModelMapperUtils modelMapperUtils;

    public UserFavoriteAnswerSubjectDocumentController(
            FavoriteAnswerSubjectDocumentService favoriteAnswerSubjectDocumentService,
            ModelMapperUtils modelMapperUtils) {
        this.favoriteAnswerSubjectDocumentService = favoriteAnswerSubjectDocumentService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> toggleFavoriteSubjectDocument(
            @PathVariable("answerSubjectDocumentId") Long answerSubjectDocumentID) {
        boolean status = favoriteAnswerSubjectDocumentService.favoriteAnswerSubjectDocument(answerSubjectDocumentID);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllFavoriteForAnswerSubjectDocument(
            @PathVariable("answerSubjectDocumentId") Long answerSubjectDocumentId) {
        List<FavoriteAnswerSubjectDocument> answerSubjectDocuments = favoriteAnswerSubjectDocumentService
                .getAllFavoriteAnswerSubjectDocument(answerSubjectDocumentId);
        return CustomResponse.generateResponse(HttpStatus.OK, answerSubjectDocuments.stream()
                .map(favorite -> modelMapperUtils.mapAllProperties(favorite, FavoriteAnswerSubjectDocumentDto.class)));
    }
}
