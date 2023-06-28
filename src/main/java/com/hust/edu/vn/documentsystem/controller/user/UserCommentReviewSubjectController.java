package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentReviewSubjectDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/subjects/reviewSubject/{reviewSubjectId}/comment")
public class UserCommentReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserCommentReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> commentReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId, @ModelAttribute CommentReviewSubjectModel commentReviewSubjectModel) {
        CommentReviewSubject commentReviewSubject = reviewSubjectService.createCommentForReviewSubject(reviewSubjectId, commentReviewSubjectModel);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentReviewSubject, CommentReviewSubjectDto.class));
    }
    @GetMapping()
    public ResponseEntity<CustomResponse> getAllCommentForReviewSubject(@PathVariable("reviewSubjectId") Long reviewSubjectId) {
        return CustomResponse.generateResponse(HttpStatus.OK, reviewSubjectService.getAllCommentForReviewSubject(reviewSubjectId).stream().map(comment -> modelMapperUtils.mapAllProperties(comment, CommentReviewSubjectDto.class)));
    }
    @PatchMapping("{commentId}")
    public ResponseEntity<CustomResponse> updateCommentForReviewSubject(@PathVariable("commentId") Long commentId, @PathVariable("reviewSubjectId") Long reviewSubjectId, @ModelAttribute CommentReviewSubjectModel commentReviewSubjectModel){
        CommentReviewSubject commentReviewSubject = reviewSubjectService.updateCommentForReviewSubject(commentId, reviewSubjectId, commentReviewSubjectModel);
        if(commentReviewSubject == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentReviewSubject, CommentReviewSubjectDto.class));
    }
    @DeleteMapping("{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentForReviewSubject(@PathVariable("commentId") Long commentId, @PathVariable("reviewSubjectId") Long reviewSubjectId, @ModelAttribute CommentReviewSubjectModel commentReviewSubjectModel){
        boolean status  = reviewSubjectService.deleteCommentForReviewSubject(commentId, reviewSubjectId, commentReviewSubjectModel);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> hiddenCommentForReviewSubject(@PathVariable("commentId") Long commentId, @PathVariable("reviewSubjectId") Long reviewSubjectId){
        boolean status = reviewSubjectService.hideCommentForReviewSubject(commentId, reviewSubjectId);
        return CustomResponse.generateResponse(status);
    }

}
