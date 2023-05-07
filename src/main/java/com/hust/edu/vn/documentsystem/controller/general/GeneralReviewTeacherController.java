package com.hust.edu.vn.documentsystem.controller.general;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/generals/teachers/reviewTeacher")
@Tag(name = "Review teacher - api")
public class GeneralReviewTeacherController {
    private final ReviewTeacherService reviewTeacherService;

    @Autowired
    public GeneralReviewTeacherController(
            ReviewTeacherService reviewTeacherService
    ) {
        this.reviewTeacherService = reviewTeacherService;
    }

    @GetMapping("{id}")
    public ResponseEntity<CustomResponse> getReviewTeacherById(@PathVariable("id") Long reviewId) {
        ReviewTeacher reviewTeacher = reviewTeacherService.getReviewTeacherById(reviewId);
        return CustomResponse.generateResponse(reviewTeacher != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST, reviewTeacher);
    }

    @PatchMapping("comment/hidden/{id}")
    public ResponseEntity<CustomResponse> hiddenComment(@PathVariable("id") Long id) {
        boolean status = reviewTeacherService.hiddenCommentForReviewTeacher(id);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("comment/active/{id}")
    public ResponseEntity<CustomResponse> activeComment(@PathVariable("id") Long id) {
        boolean status = reviewTeacherService.activeCommentForReviewTeacher(id);
        return CustomResponse.generateResponse(status);
    }

}
