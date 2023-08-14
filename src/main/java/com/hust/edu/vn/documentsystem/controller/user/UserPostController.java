package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.PageDto;
import com.hust.edu.vn.documentsystem.data.dto.PostDto;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.dto.PostInfoDto;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/posts")
@Slf4j
public class UserPostController {
    private final PostService postService;
    private final ModelMapperUtils modelMapperUtils;

    public UserPostController(PostService postService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "8") int size) {
        PageDto<PostInfoDto> pageResult = postService.getAllPostForHomePage(page, size);
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách bài viết của bạn", pageResult);
    }

    @GetMapping("{postId}")
    public ResponseEntity<CustomResponse> getPostDetail(@PathVariable("postId") Long postId) {
        Post post = postService.getPostById(postId);
        if (post == null)
            return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(post, PostDto.class));
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createPost(@ModelAttribute PostModel postModel) {
        Post post = postService.createPost(postModel);
        return CustomResponse.generateResponse(HttpStatus.OK, "Đăng bài viết thành công",
                modelMapperUtils.mapAllProperties(post, PostDto.class));
    }

    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllPostCreatedByUser() {
        List<Post> posts = postService.getAllPostCreatedByUser();
        return CustomResponse.generateResponse(HttpStatus.OK,
                posts.stream().map(post -> modelMapperUtils.mapAllProperties(post, PostDto.class)));
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<CustomResponse> deletePost(@PathVariable("postId") Long postId) {
        boolean status = postService.deletePost(postId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("{postId}")
    public ResponseEntity<CustomResponse> updatePost(@PathVariable("postId") Long postId, @ModelAttribute PostModel postModel){
        boolean status = postService.updatePost(postId, postModel);
        return CustomResponse.generateResponse(status);
    }

}
