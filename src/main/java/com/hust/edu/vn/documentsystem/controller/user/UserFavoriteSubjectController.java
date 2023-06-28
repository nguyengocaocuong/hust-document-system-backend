package com.hust.edu.vn.documentsystem.controller.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.FavoriteReviewSubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.FavoriteSubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.FavoriteSubjectModel;
import com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject;
import com.hust.edu.vn.documentsystem.entity.FavoriteSubject;
import com.hust.edu.vn.documentsystem.service.FavoriteSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

@RestController
@RequestMapping("/api/v1/users/subjects/{subjectId}/favorite")
public class UserFavoriteSubjectController {
    private final FavoriteSubjectService favoriteSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserFavoriteSubjectController(FavoriteSubjectService favoriteSubjectService, ModelMapperUtils modelMapperUtils) {
        this.favoriteSubjectService = favoriteSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> favoriteSubject(@PathVariable("subjectId") Long subjectId) {
        boolean status = favoriteSubjectService.favoriteSubject(subjectId);
        return CustomResponse.generateResponse(status);
    }
  @GetMapping()
    public ResponseEntity<CustomResponse> getAllFavoriteForSubject(
            @PathVariable("subjectId") Long subjectId) {
        List<FavoriteSubject> favoriteSubjects = favoriteSubjectService
                .getAllFavoriteForSubject(subjectId);
        return CustomResponse.generateResponse(HttpStatus.OK,
                favoriteSubjects.stream().map(favoriteSubject -> modelMapperUtils
                        .mapAllProperties(favoriteSubject, FavoriteSubjectDto.class)));
    }
}
