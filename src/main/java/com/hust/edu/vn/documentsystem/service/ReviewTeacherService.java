package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;

import java.util.Date;
import java.util.List;

public interface ReviewTeacherService {

    List<ReviewTeacher> getAllNewReviewTeacher();

    List<Object[]> getReviewForDashboard(Date sevenDaysAgo);

    List<CommentReviewTeacher> getAllCommentForReviewTeacher(Long reviewTeacherId);

    CommentReviewTeacher createCommentForReviewTeacher(Long reviewTeacherId, CommentReviewTeacherModel commentReviewTeacherModel);

    boolean deleteCommentReviewTeacher(Long commentId, Long reviewTeacherId);

    CommentReviewTeacher updateCommentReviewTeacher(Long commentId, Long reviewTeacherId, CommentReviewTeacherModel commentReviewTeacherModel);

    boolean hiddenCommentReviewTeacher(Long commentId, Long reviewTeacherId);

    ReviewTeacher createReviewTeacher(Long teacherId, ReviewTeacherModel reviewTeacherModel);

    boolean deleteReviewTeacher(Long reviewTeacherId, Long teacherId);

    boolean updateReviewTeacher(Long reviewTeacherId, Long teacherId, ReviewTeacherModel reviewTeacherModel);

    ReviewTeacher approveReviewTeacher(Long reviewTeacherId);

    ReviewTeacher rejectReviewTeacher(Long reviewTeacherId);

    boolean hiddenCommentReviewTeacher(Long commentId);

    boolean approveCommentReviewTeacher(Long commentId);

    List<CommentReviewTeacher> getAllBabComments();
}
