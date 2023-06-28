package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.service.FavoritePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/posts/{postId}/favorite")
public class UserFavoritePostController {
    private final FavoritePostService favoritePostService;

    public UserFavoritePostController(FavoritePostService favoritePostService) {
        this.favoritePostService = favoritePostService;
    }

    @GetMapping("{postId}/favorite")
    public ResponseEntity<CustomResponse> toggleFavoritePost(@PathVariable("postId") Long postId) {
        boolean status = favoritePostService.toggleFavoritePost(postId);
        return CustomResponse.generateResponse(status);
    }

}
