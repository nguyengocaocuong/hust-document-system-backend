package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.ReviewSubjectDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewSubjectModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.PropertyMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/subjects/reviewSubject")
@Tag(name="Review subject - api")
public class UserReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;
    private final ModelMapperUtils modelMapperUtils;

    public UserReviewSubjectController(ReviewSubjectService reviewSubjectService, ModelMapperUtils modelMapperUtils) {
        this.reviewSubjectService = reviewSubjectService;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping("owner")
    public ResponseEntity<CustomResponse> getAllReviewSubjectCreateByUser() {
        PropertyMap<ReviewSubject, ReviewSubjectDto> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getSubject());
                skip(destination.getOwner());
            }
        };
        Object content = reviewSubjectService.getAllReviewSubjectCreatedByUser().stream().map(reviewSubject -> modelMapperUtils.mapSelectedProperties(reviewSubject, ReviewSubjectDto.class, propertyMap));
        return CustomResponse.generateResponse(HttpStatus.OK, content);
    }


    @PostMapping()
    public ResponseEntity<CustomResponse> createReviewSubject(@ModelAttribute ReviewSubjectModel reviewSubjectModel) {
        ReviewSubject reviewSubject = reviewSubjectService.createNewReview(reviewSubjectModel);
        return CustomResponse.generateResponse(reviewSubject != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, modelMapperUtils.mapAllProperties(reviewSubject, ReviewSubjectDto.class));
    }

    @PatchMapping()
    public ResponseEntity<CustomResponse> updateReviewSubject(@ModelAttribute ReviewSubjectModel reviewSubjectModel) {
        boolean status = reviewSubjectService.updateReview(reviewSubjectModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CustomResponse> deleteReviewSubject(@PathVariable("id") Long reviewId) {
        boolean status = reviewSubjectService.deleteReviewSubjectById(reviewId);
        return CustomResponse.generateResponse(status);
    }


    @PostMapping("comment")
    public ResponseEntity<CustomResponse> createComment(@ModelAttribute CommentReviewSubjectModel commentReviewSubjectModel) {
        CommentReviewSubject comment = reviewSubjectService.createCommentForReviewSubject(commentReviewSubjectModel);
        return CustomResponse.generateResponse(comment != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, comment);
    }

    @PatchMapping("comment")
    public ResponseEntity<CustomResponse> updateComment(@ModelAttribute CommentReviewSubjectModel commentReviewSubjectModel) {
        boolean status = reviewSubjectService.updateCommentForCommentReviewSubject(commentReviewSubjectModel);
        return CustomResponse.generateResponse(status);
    }

    @DeleteMapping("comment/{id}")
    public ResponseEntity<CustomResponse> deleteComment(@PathVariable("id") Long id) {
        boolean status = reviewSubjectService.deleteCommentForReviewSubject(id);
        return CustomResponse.generateResponse(status);
    }

}
