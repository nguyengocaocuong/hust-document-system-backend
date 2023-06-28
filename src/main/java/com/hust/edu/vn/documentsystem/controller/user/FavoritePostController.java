package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/posts/{postId}/favorite")
public class FavoritePostController {
    private final PostService postService;
    private final ModelMapperUtils modelMapperUtils;

    public FavoritePostController(PostService postService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.modelMapperUtils = modelMapperUtils;
    }


}
