package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.service.FavoriteSubjectDocumentService;
import com.hust.edu.vn.documentsystem.service.SubjectDocumentService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/subjects/subjectDocument/{subjectDocumentId}/favorite")
public class UserFavoriteSubjectDocumentController {
    private final FavoriteSubjectDocumentService favoriteSubjectDocumentService;

    public UserFavoriteSubjectDocumentController(FavoriteSubjectDocumentService favoriteSubjectDocumentService) {
        this.favoriteSubjectDocumentService = favoriteSubjectDocumentService;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> favoriteSubjectDocument(
            @PathVariable("subjectDocumentId") Long subjectDocumentId) {
        boolean status = favoriteSubjectDocumentService.favoriteSubjectDocument(subjectDocumentId);
        return CustomResponse.generateResponse(status);
    }

}
