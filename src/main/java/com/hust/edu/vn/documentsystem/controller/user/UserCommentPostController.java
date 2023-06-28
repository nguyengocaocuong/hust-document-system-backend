package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentPostDto;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.entity.CommentPost;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/posts/{postId}/comment")
public class UserCommentPostController {
    private final PostService postService;

    private final ModelMapperUtils modelMapperUtils;
    public UserCommentPostController(PostService postService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllCommentForPost(@PathVariable("postId") Long postId){
        List<CommentPost> commentPosts = postService.getAllCommentForPost(postId);
        return CustomResponse.generateResponse(HttpStatus.OK, commentPosts.stream().map(comment -> modelMapperUtils.mapAllProperties(comment, CommentPostDto.class)));
    }
    @PostMapping()
    public ResponseEntity<CustomResponse> commentForPost(@PathVariable("postId") Long postId, @ModelAttribute CommentPostModel commentPostModel){
        CommentPost commentPost = postService.createComment(postId, commentPostModel);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentPost, CommentPostDto.class));
    }
    @DeleteMapping("{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentForPost(@PathVariable("commentId") Long commentId, @PathVariable("postId") Long postId){
        boolean status = postService.deleteCommentForPost(commentId, postId);
        return  CustomResponse.generateResponse(status);
    }
    @PatchMapping("{commentId}")
    public ResponseEntity<CustomResponse> updateCommentForPost(@PathVariable("commentId") Long commentId, @PathVariable("postId") Long postId, @ModelAttribute CommentPostModel commentPostModel){
        CommentPost commentPost = postService.updateCommentForPost(commentId, postId, commentPostModel);
        if(commentPost == null) CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentPost, CommentPostDto.class));
    }
    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> hiddenCommentForPost(@PathVariable("commentId") Long commentId, @PathVariable("postId") Long postId){
        boolean status = postService.hiddenCommentForPost(commentId, postId);
        return CustomResponse.generateResponse(status);
    }
}
