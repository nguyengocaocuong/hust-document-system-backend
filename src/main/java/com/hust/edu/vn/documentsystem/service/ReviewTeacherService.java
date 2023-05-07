package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;

import java.util.List;

public interface ReviewTeacherService {
    List<ReviewTeacher> findReviewByTeacherAndSubject(Long teacherId, Long subjectId);

    List<ReviewTeacher> getAllReviewTeachers();

    List<ReviewTeacher> getAllReviewTeacherCreatedByUser();

    ReviewTeacher createNewReview(ReviewTeacherModel reviewTeacherModel);

    ReviewTeacher getReviewTeacherById(Long reviewId);

    boolean updateReview(ReviewTeacherModel reviewTeacherModel);

    boolean deleteReviewTeacherById(Long reviewId);

    boolean updateCommentForCommentReviewTeacher(CommentReviewTeacherModel commentReviewTeacherModel);

    boolean deleteCommentForReviewTeacher(Long id);

    boolean hiddenCommentForReviewTeacher(Long id);

    CommentReviewTeacher createCommentForReviewTeacher(CommentReviewTeacherModel commentReviewTeacherModel);

    boolean hiddenReviewTeacher(Long id);

    boolean activeCommentForReviewTeacher(Long id);

    boolean activeReviewTeacher(Long id);

    boolean approvedReviewTeacher(Long id);

    boolean unApprovedReviewTeacher(Long id);
}
