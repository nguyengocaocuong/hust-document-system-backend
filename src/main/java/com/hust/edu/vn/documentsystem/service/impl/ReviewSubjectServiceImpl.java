package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewSubjectModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewSubjectModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.CommentReviewSubjectRepository;
import com.hust.edu.vn.documentsystem.repository.ReviewSubjectRepository;
import com.hust.edu.vn.documentsystem.repository.SubjectRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.ReviewSubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewSubjectServiceImpl implements ReviewSubjectService {
    private final ReviewSubjectRepository reviewSubjectRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final CommentReviewSubjectRepository commentReviewSubjectRepository;

    @Autowired
    public ReviewSubjectServiceImpl(
            ReviewSubjectRepository reviewSubjectRepository,
            UserRepository userRepository,
            SubjectRepository subjectRepository,
            ModelMapperUtils modelMapperUtils,
            CommentReviewSubjectRepository commentReviewSubjectRepository
    ) {
        this.reviewSubjectRepository = reviewSubjectRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.commentReviewSubjectRepository = commentReviewSubjectRepository;
    }

    @Override
    public boolean deleteReviewSubjectById(Long reviewId) {
        if (reviewId == null)
            return false;
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(reviewId).orElse(null);
        if (reviewSubject == null || !reviewSubject.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        reviewSubjectRepository.delete(reviewSubject);
        return true;
    }

    @Override
    public CommentReviewSubject createCommentForReviewSubject(CommentReviewSubjectModel commentReviewSubjectModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(commentReviewSubjectModel.getReviewSubjectId()).orElse(null);
        if (reviewSubject == null || user == null || !reviewSubject.isDone() || reviewSubject.isHidden()) return null;
        CommentReviewSubject commentReviewSubject = modelMapperUtils.mapAllProperties(commentReviewSubjectModel, CommentReviewSubject.class);
        commentReviewSubject.setReviewSubject(reviewSubject);
        commentReviewSubject.setOwner(user);
        return commentReviewSubjectRepository.save(commentReviewSubject);
    }

    @Override
    public boolean updateCommentForCommentReviewSubject(CommentReviewSubjectModel commentReviewSubjectModel) {
        if(commentReviewSubjectModel.getId() == null) return false;
        CommentReviewSubject comment = commentReviewSubjectRepository.findById(commentReviewSubjectModel.getId()).orElse(null);
        if(comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) return false;
        comment.setComment(commentReviewSubjectModel.getComment());
        comment.setRating(commentReviewSubjectModel.getRating());
        commentReviewSubjectRepository.save(comment);
        return true;
    }

    @Override
    public boolean deleteCommentForReviewSubject(Long id) {
        CommentReviewSubject comment = commentReviewSubjectRepository.findById(id).orElse(null);
        if(comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) return false;
        commentReviewSubjectRepository.delete(comment);
        return true;
    }

    @Override
    public boolean hiddenCommentForReviewSubject(Long id) {
        CommentReviewSubject comment = commentReviewSubjectRepository.findByIdAndIsHidden(id, false);
        if(comment == null || comment.getReviewSubject().getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) return false;
        comment.setHidden(true);
        commentReviewSubjectRepository.save(comment);
        return true;
    }

    @Override
    public boolean activeCommentForReviewSubject(Long id) {
        CommentReviewSubject comment = commentReviewSubjectRepository.findByIdAndIsHidden(id, true);
        if (comment == null ||!comment.getReviewSubject().getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) || !comment.isHidden()) return false;
        comment.setHidden(false);
        commentReviewSubjectRepository.save(comment);
        return true;
    }

    @Override
    public boolean hiddenReviewSubject(Long id) {
        ReviewSubject reviewSubject = reviewSubjectRepository.findByIdAndIsHidden(id, false);
        if (reviewSubject == null || reviewSubject.isHidden()) return false;
        reviewSubject.setHidden(true);
        reviewSubjectRepository.save(reviewSubject);
        return true;
    }

    @Override
    public boolean activeReviewSubject(Long id) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewSubject reviewSubject = reviewSubjectRepository.findByIdAndIsHidden(id, true);
        if (reviewSubject == null || !reviewSubject.isHidden() || user.getRoleType() != RoleType.ADMIN) return false;
        reviewSubject.setHidden(false);
        reviewSubjectRepository.save(reviewSubject);
        return true;
    }

    @Override
    public List<ReviewSubject> getAllReviewSubjects() {
        return reviewSubjectRepository.findByIsDone(true);
    }

    @Override
    public List<ReviewSubject> getAllReviewSubjectCreatedByUser() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return reviewSubjectRepository.findByOwner(user);
    }

    @Override
    public ReviewSubject getReviewSubjectById(Long reviewId) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return reviewSubjectRepository.findByIdAndIsDoneOrOwner(reviewId, true, user);
    }

    @Override
    public ReviewSubject createNewReview(ReviewSubjectModel reviewSubjectModel) {
        Subject subject = subjectRepository.findById(reviewSubjectModel.getSubjectId()).orElse(null);
        if (subject == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewSubject reviewSubject = modelMapperUtils.mapAllProperties(reviewSubjectModel, ReviewSubject.class);
        reviewSubject.setSubject(subject);
        reviewSubject.setOwner(user);
        return reviewSubjectRepository.save(reviewSubject);
    }

    @Override
    public boolean updateReview(ReviewSubjectModel reviewSubjectModel) {
        if (reviewSubjectModel.getId() == null)
            return false;
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(reviewSubjectModel.getId()).orElse(null);
        if (reviewSubject == null || !reviewSubject.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        reviewSubject.setReview(reviewSubjectModel.getReview());
        reviewSubject.setDone(reviewSubjectModel.isDone());
        reviewSubjectRepository.save(reviewSubject);
        return true;
    }

    @Override
    public boolean approvedReviewSubject(Long id) {
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(id).orElse(null);
        if( reviewSubject == null || reviewSubject.getApproved() == ApproveType.APPROVED) return false;
        reviewSubject.setApproved(ApproveType.APPROVED);
        reviewSubjectRepository.save(reviewSubject);
        return true;
    }

    @Override
    public boolean unApprovedReviewSubject(Long id) {
        ReviewSubject reviewSubject = reviewSubjectRepository.findById(id).orElse(null);
        if( reviewSubject == null || reviewSubject.getApproved() == ApproveType.REJECT) return false;
        reviewSubject.setApproved(ApproveType.REJECT);
        reviewSubjectRepository.save(reviewSubject);
        return true;
    }
}
