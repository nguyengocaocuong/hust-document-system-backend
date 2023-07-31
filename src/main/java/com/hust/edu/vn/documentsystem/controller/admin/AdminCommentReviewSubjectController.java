package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentReviewSubjectDto;
import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/admins/subjects/reviewSubject/comment")
public class AdminCommentReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public AdminCommentReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> hiddenComment(@PathVariable("commentId") Long commentId){
        boolean status = reviewSubjectService.hiddenCommentReviewSubject(commentId);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("{commentId}/approve")
    public ResponseEntity<CustomResponse> approveComment(@PathVariable("commentId") Long commentId){
        boolean status = reviewSubjectService.approveCommentReviewSubject(commentId);
        return CustomResponse.generateResponse(status);
    }
    @GetMapping("babComments")
    public ResponseEntity<CustomResponse> getAllBabComments(){
        List<CommentReviewSubject> commentReviewSubjects = reviewSubjectService.getAllBabComments();
        return CustomResponse.generateResponse(HttpStatus.OK, commentReviewSubjects.stream().map(comment -> modelMapperUtils.mapAllProperties(comment, CommentReviewSubjectDto.class)));
    }
}
