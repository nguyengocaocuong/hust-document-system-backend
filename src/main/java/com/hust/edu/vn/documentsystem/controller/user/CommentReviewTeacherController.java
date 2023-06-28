package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users/teachers/reviewTeacher/{reviewTeacherId}/comment")
public class CommentReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;

    public CommentReviewTeacherController(ReviewTeacherService reviewTeacherService, ModelMapperUtils modelMapperUtils) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllCommentForReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId) {
        List<CommentReviewTeacher> commentReviewTeachers = reviewTeacherService.getAllCommentForReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(HttpStatus.OK, commentReviewTeachers.stream().map(comment -> {
            comment.setReviewTeacher(null);
            return modelMapperUtils.mapAllProperties(comment, CommentReviewTeacherDto.class);
        }));
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createCommentForReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId, @ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        CommentReviewTeacher commentReviewTeacher = reviewTeacherService.createCommentForReviewTeacher(reviewTeacherId, commentReviewTeacherModel);
        if (commentReviewTeacher == null)
            return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentReviewTeacher, CommentReviewTeacherDto.class));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentForReviewTeacher(@PathVariable("commentId") Long commentId, @PathVariable("reviewTeacherId") Long reviewTeacherId) {
        boolean status = reviewTeacherService.deleteCommentReviewTeacher(commentId, reviewTeacherId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CustomResponse> updateCommentForReviewTeacher(@PathVariable("commentId") Long commentId, @PathVariable("reviewTeacherId") Long reviewTeacherId, @ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        boolean status = reviewTeacherService.updateCommentReviewTeacher(commentId, reviewTeacherId, commentReviewTeacherModel);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> updateCommentForReviewTeacher(@PathVariable("commentId") Long commentId, @PathVariable("reviewTeacherId") Long reviewTeacherId) {
        boolean status = reviewTeacherService.hiddenCommentReviewTeacher(commentId, reviewTeacherId);
        return CustomResponse.generateResponse(status);
    }
}
