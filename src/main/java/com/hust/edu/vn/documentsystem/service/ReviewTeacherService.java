package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;

import java.util.Date;
import java.util.List;

public interface ReviewTeacherService {
  

    ReviewTeacher getReviewTeacherById(Long reviewId);


    boolean hiddenCommentForReviewTeacher(Long id);


    boolean activeCommentForReviewTeacher(Long id);


    List<ReviewTeacher> getAllNewReviewTeacher();

    List<Object[]> getReviewForDashboard(Date sevenDaysAgo);
}
