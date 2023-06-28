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
    CommentReviewSubject createCommentForReviewSubject(Long reviewSubjectId,CommentReviewSubjectModel commentReviewSubjectModel);


    List<ReviewSubject> getAllReviewSubjects();


    boolean toggleFavoriteReviewSubject(Long reviewSubjectId);

    List<FavoriteReviewSubject> getAllFavoriteForReviewSubject(Long reviewSubjectId);


    List<ReviewSubject> getAllNewReviewSubject();
    List<Object[]> getReviewForDashboard(Date startDate);

    List<CommentReviewSubject> getAllCommentForReviewSubject(Long reviewSubjectId);

    CommentReviewSubject updateCommentForReviewSubject(Long commentId, Long reviewSubjectId, CommentReviewSubjectModel commentReviewSubjectModel);

    boolean deleteCommentForReviewSubject(Long commentId, Long reviewSubjectId, CommentReviewSubjectModel commentReviewSubjectModel);

    boolean hideCommentForReviewSubject(Long commentId, Long reviewSubjectId);

    ReviewSubject createReviewSubject(Long subjectId, ReviewSubjectModel reviewSubjectModel);

    boolean deleteReviewSubject(Long reviewSubjectId, Long subjectId);
}
