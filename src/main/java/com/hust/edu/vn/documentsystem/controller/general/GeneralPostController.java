package com.hust.edu.vn.documentsystem.controller.general;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.PostDto;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/generals/posts")
@Tag(name = "Posts - api")
public class GeneralPostController {
    private final PostService postService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public GeneralPostController(PostService postService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<CustomResponse> getPost(@PathVariable("id") Long id) {
        PostDto content = modelMapperUtils.mapAllProperties(postService.getPostById(id), PostDto.class);
        return CustomResponse.generateResponse(HttpStatus.OK, "Thông tin bài viết của bạn", content);
    }

    @PatchMapping("comment/hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenComment(@PathVariable("id") Long id) {
        boolean status = postService.hiddenCommentForPost(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("comment/active/{id}")
    public ResponseEntity<CustomResponse> activeComment(@PathVariable("id") Long id) {
        boolean status = postService.activeCommentForPost(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("answer/hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenAnswer(@PathVariable("id") Long id) {
        boolean status = postService.hiddenAnswerForPost(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("answer/active/{id}")
    public ResponseEntity<CustomResponse> activeAnswer(@PathVariable("id") Long id) {
        boolean status = postService.activeAnswerForPost(id);
        return CustomResponse.generateResponse(status);
    }

}
