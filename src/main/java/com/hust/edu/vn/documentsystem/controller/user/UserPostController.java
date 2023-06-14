package com.hust.edu.vn.documentsystem.controller.user;

import com.google.api.Http;
import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.*;
import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.data.model.CommentPostModel;
import com.hust.edu.vn.documentsystem.data.model.PostModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.service.FavoritePostService;
import com.hust.edu.vn.documentsystem.service.PostService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/posts")
@Slf4j
public class UserPostController {
    private final PostService postService;
    private final FavoritePostService favoritePostService;
    private final ModelMapperUtils modelMapperUtils;


    @Autowired
    public UserPostController(PostService postService, FavoritePostService favoritePostService, ModelMapperUtils modelMapperUtils) {
        this.postService = postService;
        this.favoritePostService = favoritePostService;
        this.modelMapperUtils = modelMapperUtils;
    }
    @GetMapping()
    public ResponseEntity<CustomResponse> getAllPosts(){
        List<Object[]> objects = postService.getAllPosts();
        List<Object> content = Collections.singletonList(objects.stream().map(object -> {
            PostDto postDto = modelMapperUtils.mapAllProperties(object[0], PostDto.class);
            postDto.setTotalAnswer((Long) object[1]);
            postDto.setTotalComment((Long) object[2]);
            postDto.setTotalFavorite((Long) object[3]);
            return postDto;
        }).toList());

        return CustomResponse.generateResponse(HttpStatus.OK, "Danh sách bài viết của bạn", content);
    }
    @PostMapping()
    public ResponseEntity<CustomResponse> createPost(@ModelAttribute PostModel postModel) {
        Post post = postService.createPost(postModel);
        return CustomResponse.generateResponse(HttpStatus.OK, "Đăng bài viết thành công", modelMapperUtils.mapAllProperties(post,  PostDto.class));
    }
    @GetMapping("{postId}")
    public ResponseEntity<CustomResponse> getPostDetail(@PathVariable("postId") Long postId){
        Post post = postService.getPostById(postId);
        if(post == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(post, PostAllDto.class));
    }
    @GetMapping("{postId}/favorite")
    public ResponseEntity<CustomResponse> toggleFavoritePost(@PathVariable("postId") Long postId){
        boolean status = favoritePostService.toggleFavoritePost(postId);
        return CustomResponse.generateResponse(status);
    }
    @GetMapping("{postId}/comment")
    public ResponseEntity<CustomResponse> getAllCommentForPost(@PathVariable("postId") Long postId){
        List<CommentPost> commentPosts = postService.getAllCommentForPost(postId);
        return CustomResponse.generateResponse(HttpStatus.OK, commentPosts.stream().map(comment -> modelMapperUtils.mapAllProperties(comment, CommentPostDto.class)));
    }
    @PostMapping("{postId}/comment")
    public ResponseEntity<CustomResponse> commentForPost(@PathVariable("postId") Long postId, @ModelAttribute CommentPostModel commentPostModel){
        CommentPost commentPost = postService.createComment(postId, commentPostModel);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentPost, CommentPostDto.class));
    }
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentForPost(@PathVariable("commentId") Long commentId){
        boolean status = postService.deleteCommentForPost(commentId);
        return  CustomResponse.generateResponse(status);
    }
    @PatchMapping("comment/{commentId}")
    public ResponseEntity<CustomResponse> updateCommentForPost(@PathVariable("commentId") Long commentId, @ModelAttribute CommentPostModel commentPostModel){
        CommentPost commentPost = postService.updateCommentForPost(commentId, commentPostModel);
        if(commentPost == null) CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, commentPost);
    }
    @GetMapping("{postId}/answer")
    public ResponseEntity<CustomResponse> getAllAnswerForPost(@PathVariable("postId") Long postId){
        List<AnswerPost> answerPosts = postService.findAllAnswerForPost(postId);
        return CustomResponse.generateResponse(HttpStatus.OK, answerPosts.stream().map(answer -> modelMapperUtils.mapAllProperties(answer, AnswerPostDto.class)));
    }
    @PostMapping("{postId}/answer")
    public ResponseEntity<CustomResponse> createAnswerForPost(@PathVariable("postId") Long postId, @ModelAttribute AnswerPostModel answerPostModel){
        AnswerPost answerPost = postService.createAnswerForPost(postId, answerPostModel);
        if(answerPost == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(answerPost, AnswerPostDto.class));
    }
    @PostMapping("answer/{answerId}/favorite")
    public ResponseEntity<CustomResponse> toggleFavoriteAnswerPost(@PathVariable("answerId") Long answerId){
        boolean status = postService.toggleFavoriteAnswerPost(answerId);
        return CustomResponse.generateResponse(status);
    }


    @GetMapping("answer/{answerId}/favorite")
    public ResponseEntity<CustomResponse> getAllFavoriteForAnswer(@PathVariable("answerId") Long answerId){
        List<FavoriteAnswerPost> favoriteAnswerPostList = postService.getAllFavoriteForAnswer(answerId);
        return CustomResponse.generateResponse(HttpStatus.OK, favoriteAnswerPostList.stream().map(favorite -> modelMapperUtils.mapAllProperties(favorite, FavoriteAnswerPostDto.class)));
    }

    @GetMapping("{postId}/translate")
    public ResponseEntity<String> translateDocument(@PathVariable("postId") Long postId, @RequestParam("targetLanguage") TargetLanguageType targetLanguageType){
        List<Object> data = postService.translatePost(postId, targetLanguageType);
        if (data == null ) return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body((String)data.get(1));
    }

    @GetMapping("answer/{answerId}/readFile")
    public ResponseEntity<Resource> readAnswerPost(@PathVariable("answerId") Long answerId) {
        List<Object> data = postService.readAnswerPost(answerId);
        if (data == null || ((byte[]) data.get(1)).length == 0) return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }

    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllPostCreatedByUser(){
        List<Post> posts = postService.getAllPostCreatedByUser();
        return CustomResponse.generateResponse(HttpStatus.OK, posts.stream().map(post -> modelMapperUtils.mapAllProperties(post, PostDto.class)));
    }
    @DeleteMapping("{postId}")
    public ResponseEntity<CustomResponse> deletePost(@PathVariable("postId") Long postId){
        boolean status = postService.deletePost(postId);
        return CustomResponse.generateResponse(status);
    }

}
