package com.hust.edu.vn.documentsystem.controller.user;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.CommentReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.dto.FavoriteReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.FavoriteReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users/teachers")
@Slf4j
public class UserTeacherController {
    private final TeacherService teacherService;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    public UserTeacherController(TeacherService teacherService, ModelMapperUtils modelMapperUtils) {
        this.modelMapperUtils = modelMapperUtils;
        this.teacherService = teacherService;
    }


    @GetMapping("allTeacherForFilter")
    public ResponseEntity<CustomResponse> getAllSubjectForFilter() {
        return CustomResponse.generateResponse(HttpStatus.OK, teacherService.getAllTeachersForFilter());
    }

    @PostMapping()
    public ResponseEntity<CustomResponse> createTeacher(@ModelAttribute TeacherModel teacherModel) {
        log.info(teacherModel.toString());
        Teacher teacher = teacherService.createTeacher(teacherModel);
        teacher.setSubjects(null);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(teacher, TeacherDto.class));
    }

    @GetMapping("/reviewTeacher")
    public ResponseEntity<CustomResponse> getAllReviewTeacher() {
        List<ReviewTeacher> reviewTeacherList = teacherService.findAllReviewTeacher();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeacherList.stream().map(review -> {
            review.getTeacher().setSubjects(null);
            return modelMapperUtils.mapAllProperties(review, ReviewTeacherDto.class);
        }));
    }

    @PostMapping ("/reviewTeacher/{reviewTeacherId}/favorite")
    public ResponseEntity<CustomResponse> toggleFavoriteReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId) {
        boolean status = teacherService.toggleFavoriteReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(status);
    }
    @GetMapping ("/reviewTeacher/{reviewTeacherId}/favorite")
    public ResponseEntity<CustomResponse> getAllFavoriteReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId) {
        List<FavoriteReviewTeacher> favoriteReviewTeacher = teacherService.getAllFavoriteReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(HttpStatus.OK, favoriteReviewTeacher.stream().map(favorite -> modelMapperUtils.mapAllProperties(favorite, FavoriteReviewTeacherDto.class)));
    }

    @PostMapping("/reviewTeacher/{reviewTeacherId}/comment")
    public ResponseEntity<CustomResponse> createCommentForReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId, @ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        CommentReviewTeacher commentReviewTeacher = teacherService.createCommentForReviewTeacher(reviewTeacherId, commentReviewTeacherModel);
        if (commentReviewTeacher == null)
            return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        return CustomResponse.generateResponse(HttpStatus.OK, modelMapperUtils.mapAllProperties(commentReviewTeacher, CommentReviewTeacherDto.class));
    }

    @DeleteMapping("/reviewTeacher/comment/{commentId}")
    public ResponseEntity<CustomResponse> deleteCommentForReviewTeacher(@PathVariable("commentId") Long commentId) {
        boolean status = teacherService.deleteCommentReview(commentId);
        return CustomResponse.generateResponse(status);
    }

    @PatchMapping("/reviewTeacher/comment/{commentId}")
    public ResponseEntity<CustomResponse> updateCommentForReviewTeacher(@PathVariable("commentId") Long commentId, @ModelAttribute CommentReviewTeacherModel commentReviewTeacherModel) {
        boolean status = teacherService.updateCommentReview(commentId, commentReviewTeacherModel);
        return CustomResponse.generateResponse(status);
    }

    @GetMapping("/reviewTeacher/{reviewTeacherId}/comment")
    public ResponseEntity<CustomResponse> getAllCommentForReviewTeacher(@PathVariable("reviewTeacherId") Long reviewTeacherId) {
        List<CommentReviewTeacher> commentReviewTeachers = teacherService.getAllCommentForReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(HttpStatus.OK, commentReviewTeachers.stream().map(comment -> {comment.setReviewTeacher(null); return modelMapperUtils.mapAllProperties(comment, CommentReviewTeacherDto.class);}));
    }
    @DeleteMapping("reviewTeacher/{reviewTeacherId}")
    public ResponseEntity<CustomResponse> deleteReviewSubject(@PathVariable("reviewTeacherId") Long reviewTeacherId){
        boolean status = teacherService.deleteReviewTeacher(reviewTeacherId);
        return CustomResponse.generateResponse(status);
    }
    @GetMapping("reviewTeacher/owner")
    public ResponseEntity<CustomResponse> getAllReviewTeacherCreatedByUser(){
        List<ReviewTeacher> reviewTeachers = teacherService.getAllReviewTeacherCreatedByUser();
        return CustomResponse.generateResponse(HttpStatus.OK, reviewTeachers.stream().map(review -> modelMapperUtils.mapAllProperties(review, ReviewTeacherDto.class)));
    }

}
