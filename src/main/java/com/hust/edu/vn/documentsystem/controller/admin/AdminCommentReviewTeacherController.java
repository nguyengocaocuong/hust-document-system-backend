package com.hust.edu.vn.documentsystem.controller.admin;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/admins/teachers/reviewTeacher/comment")
public class AdminCommentReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;

    private final ModelMapperUtils modelMapperUtils;

    public AdminCommentReviewTeacherController(ReviewTeacherService reviewTeacherService, ModelMapperUtils modelMapperUtils) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }


    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> hiddenComment(@PathVariable("commentId") Long commentId){
        boolean status = reviewTeacherService.hiddenCommentReviewTeacher(commentId);
        return CustomResponse.generateResponse(status);
    }
    @PatchMapping("{commentId}/approve")
    public ResponseEntity<CustomResponse> approveComment(@PathVariable("commentId") Long commentId){
        boolean status = reviewTeacherService.approveCommentReviewTeacher(commentId);
        return CustomResponse.generateResponse(status);
    }
    @GetMapping("babComments")
    public ResponseEntity<CustomResponse> getAllBabComments(){
        List<CommentReviewTeacher> commentReviewSubjects = reviewTeacherService.getAllBabComments();
        return CustomResponse.generateResponse(HttpStatus.OK, commentReviewSubjects.stream().map(comment -> modelMapperUtils.mapAllProperties(comment, CommentReviewTeacherDto.class)));
    }
}
