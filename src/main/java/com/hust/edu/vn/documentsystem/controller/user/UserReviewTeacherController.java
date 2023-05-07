package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/teachers/reviewTeacher")
@Tag(name = "Review teacher - api")
public class UserReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public UserReviewTeacherController(
            ReviewTeacherService reviewTeacherService,
            ModelMapperUtils modelMapperUtils
            ) {
        this.reviewTeacherService = reviewTeacherService;
        this.modelMapperUtils = modelMapperUtils;
    }


    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllReviewTeacherCreateByUser() {
        PropertyMap<ReviewTeacher, ReviewTeacherDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getTeacher());
                skip(destination.getOwner());
            }
        };
        Object content = reviewTeacherService.getAllReviewTeacherCreatedByUser().stream().map(reviewTeacher -> modelMapperUtils.mapSelectedProperties(reviewTeacher, ReviewTeacherDto.class, propertyMap));
        return CustomResponse.generateResponse(HttpStatus.OK, content);
    }


    @PostMapping()
    public ResponseEntity<CustomResponse> createReviewTeacher(@ModelAttribute ReviewTeacherModel reviewTeacherModel) {
        ReviewTeacher reviewTeacher = reviewTeacherService.createNewReview(reviewTeacherModel);
        return CustomResponse.generateResponse(reviewTeacher != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, modelMapperUtils.mapAllProperties(reviewTeacher, ReviewTeacherDto.class));
    }

    @PatchMapping()
    public ResponseEntity<CustomResponse> updateReviewTeacher(@ModelAttribute ReviewTeacherModel reviewTeacherModel) {
        boolean status = reviewTeacherService.updateReview(reviewTeacherModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CustomResponse> deleteReviewTeacher(@PathVariable("id") Long reviewId) {
        boolean status = reviewTeacherService.deleteReviewTeacherById(reviewId);
        return CustomResponse.generateResponse(status);
    }


    @PostMapping("comment")
    public ResponseEntity<CustomResponse> createComment(@ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        CommentReviewTeacher comment = reviewTeacherService.createCommentForReviewTeacher(commentReviewTeacherModel);
        return CustomResponse.generateResponse(comment != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, modelMapperUtils.mapAllProperties(comment, CommentReviewTeacherDto.class));
    }

    @PatchMapping("comment")
    public ResponseEntity<CustomResponse> updateComment(@ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        boolean status = reviewTeacherService.updateCommentForCommentReviewTeacher(commentReviewTeacherModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("comment/{id}")
    public ResponseEntity<CustomResponse> deleteComment(@PathVariable("id") Long id) {
        boolean status = reviewTeacherService.deleteCommentForReviewTeacher(id);
        return CustomResponse.generateResponse(status);
    }

}
