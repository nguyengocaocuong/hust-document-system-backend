package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.AnswerPostDto;
import com.hust.edu.vn.documentsystem.data.model.AnswerPostModel;
import com.hust.edu.vn.documentsystem.entity.AnswerPost;
import com.hust.edu.vn.documentsystem.entity.Document;
import com.hust.edu.vn.documentsystem.service.AnswerPostService;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/posts/{postId}/answer")
public class UserAnswerPostController {
    private final AnswerPostService answerPostService;
    private final ModelMapperUtils modelMapperUtils;

    private final PusherService pusherService;

    public UserAnswerPostController(AnswerPostService answerPostService, ModelMapperUtils modelMapperUtils, PusherService pusherService) {
        this.answerPostService = answerPostService;
        this.modelMapperUtils = modelMapperUtils;
        this.pusherService = pusherService;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllAnswerForPost(@PathVariable("postId") Long postId) {
        List<AnswerPost> answerPosts = answerPostService.findAllAnswerForPost(postId);
        return CustomResponse.generateResponse(HttpStatus.OK,
                answerPosts.stream().map(answer -> modelMapperUtils.mapAllProperties(answer, AnswerPostDto.class)));
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createAnswerForPost(@PathVariable("postId") Long postId,
                                                              @ModelAttribute AnswerPostModel answerPostModel) {
        AnswerPost answerPost = answerPostService.createAnswerForPost(postId, answerPostModel);
        if (answerPost == null)
            return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        AnswerPostDto answerPostDto = modelMapperUtils.mapAllProperties(answerPost, AnswerPostDto.class);
        pusherService.triggerChanel("answer-post-" + postId, "new-answer", answerPostDto);
        return CustomResponse.generateResponse(HttpStatus.OK, answerPostDto);
    }

    @GetMapping("{answerId}/readFile")
    public ResponseEntity<Resource> readAnswerPost(@PathVariable("answerId") Long answerId,
                                                   @PathVariable("postId") Long postId) {
        List<Object> data = answerPostService.readAnswerPost(answerId, postId);
        if (data == null || ((byte[]) data.get(1)).length == 0)
            return ResponseEntity.notFound().build();
        Document document = (Document) data.get(0);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(document.getName()).build());
        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource((byte[]) data.get(1)));
    }

    @DeleteMapping("{answerId}")
    public ResponseEntity<CustomResponse> deleteAnswerForPost(@PathVariable("answerId") Long answerId,
                                                              @PathVariable("postId") Long postId) {
        boolean status = answerPostService.deleteAnswerForPost(answerId, postId);
        return CustomResponse.generateResponse(status);
    }

}
