package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/posts/{postId}/answer")
public class AnswerPostController {
    private final PostService postService;
    private final ModelMapperUtils modelMapperUtils;

    public AnswerPostController(PostService postService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.modelMapperUtils = modelMapperUtils;
    }

}
