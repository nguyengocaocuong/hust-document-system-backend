package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewSubjectModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewSubjectServiceImpl implements ReviewSubjectService {
    private final ReviewSubjectRepository reviewSubjectRepository;
    private final UserRepository userRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final CommentReviewSubjectRepository commentReviewSubjectRepository;
    private final SubjectRepository subjectRepository;

    public ReviewSubjectServiceImpl(
            ReviewSubjectRepository reviewSubjectRepository,
            UserRepository userRepository,
            ModelMapperUtils modelMapperUtils,
            CommentReviewSubjectRepository commentReviewSubjectRepository,
            SubjectRepository subjectRepository) {
        this.reviewSubjectRepository = reviewSubjectRepository;
        this.userRepository = userRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.commentReviewSubjectRepository = commentReviewSubjectRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public CommentReviewSubject createCommentForReviewSubject(Long reviewSubjectId,
            CommentReviewSubjectModel commentReviewSubjectModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(reviewSubjectId).orElse(null);
        if (reviewSubject == null || user == null || !reviewSubject.isDone() || reviewSubject.isHidden())
            return null;
        CommentReviewSubject commentReviewSubject = modelMapperUtils.mapAllProperties(commentReviewSubjectModel,
                CommentReviewSubject.class);
        if (commentReviewSubjectModel.getParentCommentId() != null) {
            CommentReviewSubject parent = commentReviewSubjectRepository
                    .findById(commentReviewSubjectModel.getParentCommentId()).orElse(null);
            if (parent == null)
                return null;
            commentReviewSubject.setParentComment(parent);
        }
        commentReviewSubject.setReviewSubject(reviewSubject);
        commentReviewSubject.setOwner(user);
        return commentReviewSubjectRepository.save(commentReviewSubject);
    }

    @Override
    public List<ReviewSubject> getAllReviewSubjects() {
        return reviewSubjectRepository.findAllByIsDone(true);
    }

    @Override
    public List<ReviewSubject> getAllNewReviewSubject() {
        return reviewSubjectRepository.findAllNewReviewSubject();
    }

    @Override
    public List<Object[]> getReviewForDashboard(Date startDate) {
        return reviewSubjectRepository.getReviewForDashboard(startDate);
    }

    @Override
    public List<CommentReviewSubject> getAllCommentForReviewSubject(Long reviewSubjectId) {
        return commentReviewSubjectRepository.findAllByReviewSubjectId(reviewSubjectId);
    }

    @Override
    public CommentReviewSubject updateCommentForReviewSubject(Long commentId, Long reviewSubjectId,
            CommentReviewSubjectModel commentReviewSubjectModel) {
        CommentReviewSubject commentReviewSubject = commentReviewSubjectRepository
                .findByIdAndReviewSubjectIdAndOwnerEmail(commentId, reviewSubjectId,
                        SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentReviewSubject == null)
            return null;
        commentReviewSubject.setComment(commentReviewSubjectModel.getComment());
        return commentReviewSubjectRepository.save(commentReviewSubject);
    }

    @Override
    public boolean deleteCommentForReviewSubject(Long commentId, Long reviewSubjectId,
            CommentReviewSubjectModel commentReviewSubjectModel) {
        CommentReviewSubject commentReviewSubject = commentReviewSubjectRepository
                .findByIdAndReviewSubjectIdAndOwnerEmail(commentId, reviewSubjectId,
                        SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentReviewSubject == null)
            return false;
        commentReviewSubjectRepository.delete(commentReviewSubject);
        return true;
    }

    @Override
    public boolean hideCommentForReviewSubject(Long commentId, Long reviewSubjectId) {
        CommentReviewSubject commentReviewSubject = commentReviewSubjectRepository
                .findByIdAndReviewSubjectIdAndHiddenAndOwnerReviewSubjectEmail(commentId, reviewSubjectId,
                        SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentReviewSubject == null)
            return false;
        commentReviewSubject.setHidden(true);
        commentReviewSubjectRepository.save(commentReviewSubject);
        return true;
    }

    @Override
    public ReviewSubject createReviewSubject(Long subjectId, ReviewSubjectModel reviewSubjectModel) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewSubject reviewSubject = new ReviewSubject();
        reviewSubject.setReview(reviewSubjectModel.getReview());
        reviewSubject.setDone(reviewSubjectModel.getDone() == 1);
        reviewSubject.setSubject(subject);
        reviewSubject.setOwner(user);
        return reviewSubjectRepository.save(reviewSubject);
    }

    @Override
    public boolean deleteReviewSubject(Long reviewSubjectId, Long subjectId) {
        ReviewSubject reviewSubject = reviewSubjectRepository.findByIdAndSubjectIdAndUserEmail(reviewSubjectId,
                subjectId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (reviewSubject == null)
            return false;
        reviewSubjectRepository.deleteById(reviewSubject.getId());
        return true;
    }

    @Override
    public boolean updateReviewSubject(Long reviewSubjectId, Long subjectId, ReviewSubjectModel reviewSubjectModel) {
        ReviewSubject reviewSubject  = reviewSubjectRepository.findByIdAndSubjectIdAndUserEmail(reviewSubjectId, subjectId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(reviewSubject == null) return false;
        reviewSubject.setDone(reviewSubjectModel.getDone() == 1);
        reviewSubject.setReview(reviewSubjectModel.getReview());
        reviewSubject.setApproved(ApproveType.NEW);
        reviewSubjectRepository.save(reviewSubject);
        return true;
    }
}
