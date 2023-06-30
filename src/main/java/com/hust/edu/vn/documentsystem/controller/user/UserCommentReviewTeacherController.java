package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/teachers/reviewTeacher/{reviewTeacherId}/comment")
public class UserCommentReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;
    private final PusherService pusherService;

    public UserCommentReviewTeacherController(ReviewTeacherService reviewTeacherService,
            ModelMapperUtils modelMapperUtils, PusherService pusherService) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
        this.pusherService = pusherService;
    }

    @GetMapping()
    public ResponseEntity<CustomResponse> getAllCommentForReviewTeacher(
            @PathVariable("reviewTeacherId") Long reviewTeacherId) {
        List<CommentReviewTeacher> commentReviewTeachers = reviewTeacherService
                .getAllCommentForReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(HttpStatus.OK, commentReviewTeachers.stream().map(comment -> {
            comment.setReviewTeacher(null);
            return modelMapperUtils.mapAllProperties(comment, CommentReviewTeacherDto.class);
        }));
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createCommentForReviewTeacher(
            @PathVariable("reviewTeacherId") Long reviewTeacherId,
            @ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        CommentReviewTeacher commentReviewTeacher = reviewTeacherService.createCommentForReviewTeacher(reviewTeacherId,
                commentReviewTeacherModel);
        if (commentReviewTeacher == null)
            return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        CommentReviewTeacherDto commentReviewTeacherDto = modelMapperUtils.mapAllProperties(commentReviewTeacher,
                CommentReviewTeacherDto.class);
        pusherService.triggerChanel("comment-review-teacher-" + reviewTeacherId, "new-comment",
                commentReviewTeacherDto);
        return CustomResponse.generateResponse(HttpStatus.OK, commentReviewTeacherDto);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentForReviewTeacher(@PathVariable("commentId") Long commentId,
            @PathVariable("reviewTeacherId") Long reviewTeacherId) {
        boolean status = reviewTeacherService.deleteCommentReviewTeacher(commentId, reviewTeacherId);
        if (status)
            pusherService.triggerChanel("comment-review-teacher-" + reviewTeacherId, "delete-comment",
                    commentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CustomResponse> updateCommentForReviewTeacher(@PathVariable("commentId") Long commentId,
            @PathVariable("reviewTeacherId") Long reviewTeacherId,
            @ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        boolean status = reviewTeacherService.updateCommentReviewTeacher(commentId, reviewTeacherId,
                commentReviewTeacherModel);
        if (status)
            pusherService.triggerChanel("comment-review-teacher-" + reviewTeacherId, "edit-comment",
                    commentReviewTeacherModel);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("{commentId}/hidden")
    public ResponseEntity<CustomResponse> updateCommentForReviewTeacher(@PathVariable("commentId") Long commentId,
            @PathVariable("reviewTeacherId") Long reviewTeacherId) {
        boolean status = reviewTeacherService.hiddenCommentReviewTeacher(commentId, reviewTeacherId);
        if (status)
            pusherService.triggerChanel("comment-review-teacher-" + reviewTeacherId, "hidden-comment",
                    commentId);
        return CustomResponse.generateResponse(status);
    }
}
