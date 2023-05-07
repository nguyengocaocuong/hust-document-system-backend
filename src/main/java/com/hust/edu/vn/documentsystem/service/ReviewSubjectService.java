package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.CommentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewSubjectModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;

import java.util.List;

public interface ReviewSubjectService {
    boolean deleteReviewSubjectById(Long reviewId);

    CommentReviewSubject createCommentForReviewSubject(CommentReviewSubjectModel commentReviewSubjectModel);

    boolean updateCommentForCommentReviewSubject(CommentReviewSubjectModel commentReviewSubjectModel);

    boolean deleteCommentForReviewSubject(Long id);

    boolean hiddenCommentForReviewSubject(Long id);

    boolean activeCommentForReviewSubject(Long id);

    boolean hiddenReviewSubject(Long id);

    boolean activeReviewSubject(Long id);

    List<ReviewSubject> getAllReviewSubjects();

    List<ReviewSubject> getAllReviewSubjectCreatedByUser();

    ReviewSubject getReviewSubjectById(Long reviewId);

    ReviewSubject createNewReview(ReviewSubjectModel reviewSubjectModel);

    boolean updateReview(ReviewSubjectModel reviewSubjectModel);

    boolean approvedReviewSubject(Long id);

    boolean unApprovedReviewSubject(Long id);
}
