package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.FavoriteReviewTeacherDto;
import com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher;
import com.hust.edu.vn.documentsystem.service.FavoriteReviewTeacherService;
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
@RequestMapping("/api/v1/users/teachers/reviewTeacher/{reviewTeacherId}/favorite")
public class UserFavoriteReviewTeacherController {
    private final FavoriteReviewTeacherService favoriteReviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;

    public UserFavoriteReviewTeacherController(FavoriteReviewTeacherService favoriteReviewTeacherService,
            ModelMapperUtils modelMapperUtils) {
        this.favoriteReviewTeacherService = favoriteReviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> toggleFavoriteReviewTeacher(
            @PathVariable("reviewTeacherId") Long reviewTeacherId) {
        boolean status = favoriteReviewTeacherService.toggleFavoriteReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllFavoriteReviewTeacher(
            @PathVariable("reviewTeacherId") Long reviewTeacherId) {
        List<FavoriteReviewTeacher> favoriteReviewTeacher = favoriteReviewTeacherService
                .getAllFavoriteReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(HttpStatus.OK, favoriteReviewTeacher.stream()
                .map(favorite -> modelMapperUtils.mapAllProperties(favorite, FavoriteReviewTeacherDto.class)));
    }

}
