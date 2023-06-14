package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.dto.ReviewSubjectDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewSubjectModel;
import com.hust.edu.vn.documentsystem.entity.CommentReviewSubject;
import com.hust.edu.vn.documentsystem.entity.FavoriteReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ReviewSubjectService {
    boolean deleteReviewSubjectById(Long reviewId);

    CommentReviewSubject createCommentForReviewSubject(Long reviewSubjectId,CommentReviewSubjectModel commentReviewSubjectModel);

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

    boolean toggleFavoriteReviewSubject(Long reviewSubjectId);

    List<FavoriteReviewSubject> getAllFavoriteForReviewSubject(Long reviewSubjectId);

    List<ReviewSubject> getAllReviewSubjectsCreateByUser();

    List<ReviewSubject> getAllNewReviewSubject();
    List<Object[]> getReviewForDashboard(Date startDate);
}
