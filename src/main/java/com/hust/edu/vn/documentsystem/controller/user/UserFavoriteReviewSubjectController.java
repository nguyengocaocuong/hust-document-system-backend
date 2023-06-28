package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.FavoriteReviewSubjectDto;
import com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject;
import com.hust.edu.vn.documentsystem.service.FavoriteReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/subjects/reviewSubject/{reviewSubjectId}/favorite")
public class UserFavoriteReviewSubjectController {
    private final FavoriteReviewSubjectService favoriteReviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserFavoriteReviewSubjectController(FavoriteReviewSubjectService favoriteReviewSubjectService,
            ModelMapperUtils modelMapperUtils) {
        this.favoriteReviewSubjectService = favoriteReviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> favoriteReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId) {
        boolean status = favoriteReviewSubjectService.toggleFavoriteReviewSubject(reviewSubjectId);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllFavoriteForReviewSubject(
            @PathVariable("reviewSubjectId") Long reviewSubjectId) {
        List<FavoriteReviewSubject> favoriteForReviewSubject = favoriteReviewSubjectService
                .getAllFavoriteForReviewSubject(reviewSubjectId);
        return CustomResponse.generateResponse(HttpStatus.OK,
                favoriteForReviewSubject.stream().map(favoriteReviewSubject -> modelMapperUtils
                        .mapAllProperties(favoriteReviewSubject, FavoriteReviewSubjectDto.class)));
    }

}
