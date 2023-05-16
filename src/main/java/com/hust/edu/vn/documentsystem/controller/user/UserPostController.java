package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.PostDto;
import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/posts")
@Tag(name = "Posts - api")
public class UserPostController {
    private final PostService postService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public UserPostController(PostService postService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.modelMapperUtils = modelMapperUtils;
    }
    @GetMapping()
    public ResponseEntity<CustomResponse> getAllPosts(){
        List<Object> content = postService.getAllPosts().stream().map(post -> modelMapperUtils.mapAllProperties(post, PostDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách bài viết của bạn", content);
    }

    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllPostsCreateByUser() {
        List<Object> content = postService.getAllPostsCreateByUser().stream().map(post -> modelMapperUtils.mapAllProperties(post, PostDto.class)).toList();
        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách bài viết của bạn", content);
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createPost(@ModelAttribute PostModel postModel) {
        Post post = postService.createPost(postModel);
        return CustomResponse.generateResponse(HttpStatus.OK, "Đăng bài viết thành công", post);
    }

    @PatchMapping()
    public ResponseEntity<CustomResponse> updatePost(@ModelAttribute PostModel postModel) {
        boolean status = postService.updatePost(postModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<CustomResponse> deletePost(@PathVariable("id") Long postId) {
        boolean isDeleted = postService.deletePost(postId);
        return CustomResponse.generateResponse(isDeleted);
    }


    @PostMapping("answer")
    public ResponseEntity<CustomResponse> uploadAnswer(@ModelAttribute AnswerPostModel answerPostModel) {
        boolean status = postService.uploadAnswer(answerPostModel);
        return CustomResponse.generateResponse(status);
    }

    @PostMapping("comment")
    public ResponseEntity<CustomResponse> createComment(@ModelAttribute CommentPostModel commentPostModel) {
        boolean status = postService.createComment(commentPostModel);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("comment")
    public ResponseEntity<CustomResponse> updateComment(@ModelAttribute CommentPostModel commentPostModel) {
        boolean status = postService.updateCommentForPost(commentPostModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("comment/{id}")
    public ResponseEntity<CustomResponse> deleteComment(@PathVariable("id") Long id) {
        boolean status = postService.deleteCommentForPost(id);
        return CustomResponse.generateResponse(status);
    }
}
