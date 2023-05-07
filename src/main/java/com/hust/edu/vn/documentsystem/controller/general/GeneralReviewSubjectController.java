package com.hust.edu.vn.documentsystem.controller.general;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/generals/subjects/reviewSubject")
@Tag(name = "Review subject - api")
public class GeneralReviewSubjectController {
    private final ReviewSubjectService reviewSubjectService;

    @Autowired
    public GeneralReviewSubjectController(ReviewSubjectService reviewSubjectService) {
        this.reviewSubjectService = reviewSubjectService;
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getReviewSubjectById(@PathVariable("id") Long reviewId) {
        ReviewSubject reviewSubject = reviewSubjectService.getReviewSubjectById(reviewId);
        return CustomResponse.generateResponse(reviewSubject != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, reviewSubject);
    }

    @PatchMapping("comment/hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenComment(@PathVariable("id") Long id) {
        boolean status = reviewSubjectService.hiddenCommentForReviewSubject(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("comment/active/{id}")
    public ResponseEntity<CustomResponse> activeComment(@PathVariable("id") Long id) {
        boolean status = reviewSubjectService.activeCommentForReviewSubject(id);
        return CustomResponse.generateResponse(status);
    }

}
