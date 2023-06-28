package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.FavoriteAnswerPostDto;
import com.hust.edu.vn.documentsystem.entity.FavoriteAnswerPost;
import com.hust.edu.vn.documentsystem.service.FavoriteAnswerPostService;
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
@RequestMapping("/api/v1/users/posts/answer/{answerId}/favorite")
public class UserFavoriteAnswerPostController {
    private final FavoriteAnswerPostService favoriteAnswerPostService;
    private final ModelMapperUtils modelMapperUtils;

    public UserFavoriteAnswerPostController(FavoriteAnswerPostService favoriteAnswerPostService, ModelMapperUtils modelMapperUtils) {
        this.favoriteAnswerPostService = favoriteAnswerPostService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> toggleFavoriteAnswerPost(@PathVariable("answerId") Long answerId) {
        boolean status = favoriteAnswerPostService.toggleFavoriteAnswerPost(answerId);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllFavoriteForAnswer(@PathVariable("answerId") Long answerId) {
        List<FavoriteAnswerPost> favoriteAnswerPostList = favoriteAnswerPostService.getAllFavoriteForAnswer(answerId);
        return CustomResponse.generateResponse(HttpStatus.OK, favoriteAnswerPostList.stream()
                .map(favorite -> modelMapperUtils.mapAllProperties(favorite, FavoriteAnswerPostDto.class)));
    }
}
