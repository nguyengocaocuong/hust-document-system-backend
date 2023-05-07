package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.PostDto;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins/posts")
@Tag(name = "Posts - api")
public class AdminPostController {
    private final PostService postService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public AdminPostController(PostService postService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllPosts() {
        List<Object> content = postService.getAllPosts().stream().map(post -> modelMapperUtils.mapAllProperties(post, PostDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách bài viết", content);
    }

    @PatchMapping("hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenPost(@PathVariable("id") Long id) {
        boolean status = postService.hiddenPost(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("active/{id}")
    public ResponseEntity<CustomResponse> activePost(@PathVariable("id") Long id) {
        boolean status = postService.activePost(id);
        return CustomResponse.generateResponse(status ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
