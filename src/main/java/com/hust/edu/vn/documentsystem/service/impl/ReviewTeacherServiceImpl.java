package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.CommentReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.ReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.TeacherRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.PusherService;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ReviewTeacherServiceImpl implements ReviewTeacherService {
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final CommentReviewTeacherRepository commentReviewTeacherRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final PusherService pusherService;


    public ReviewTeacherServiceImpl(
            ReviewTeacherRepository reviewTeacherRepository,
            CommentReviewTeacherRepository commentReviewTeacherRepository,
            UserRepository userRepository,
            TeacherRepository teacherRepository, PusherService pusherService) {
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.commentReviewTeacherRepository = commentReviewTeacherRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.pusherService = pusherService;
    }


    @Override
    public List<ReviewTeacher> getAllNewReviewTeacher() {
        return reviewTeacherRepository.findAllNewReviewTeacher();
    }

    @Override
    public List<Object[]> getReviewForDashboard(Date sevenDaysAgo) {
        return reviewTeacherRepository.getReviewForDashboard(sevenDaysAgo);
    }

    @Override
    public List<CommentReviewTeacher> getAllCommentForReviewTeacher(Long reviewTeacherId) {
        return commentReviewTeacherRepository.findAllByIdAndHidden(reviewTeacherId, false);
    }

    @Override
    public CommentReviewTeacher createCommentForReviewTeacher(Long reviewTeacherId, CommentReviewTeacherModel commentReviewTeacherModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewTeacherId).orElse(null);
        if (reviewTeacher == null) return null;
        CommentReviewTeacher commentReviewTeacher = new CommentReviewTeacher();
        commentReviewTeacher.setReviewTeacher(reviewTeacher);
        commentReviewTeacher.setComment(commentReviewTeacherModel.getComment());
        commentReviewTeacher.setOwner(user);
        if (commentReviewTeacherModel.getParentCommentId() != null) {
            CommentReviewTeacher parent = commentReviewTeacherRepository.findById(commentReviewTeacherModel.getParentCommentId()).orElse(null);
            if (parent == null) return null;
            commentReviewTeacher.setParentComment(parent);
        }
        return commentReviewTeacherRepository.save(commentReviewTeacher);
    }

    @Override
    public boolean deleteCommentReviewTeacher(Long commentId, Long reviewTeacherId) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndReviewTeacherIdAndOwnerEmail(commentId, reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentReviewTeacher == null) return false;
        commentReviewTeacherRepository.delete(commentReviewTeacher);
        return true;
    }

    @Override
    public CommentReviewTeacher updateCommentReviewTeacher(Long commentId, Long reviewTeacherId, CommentReviewTeacherModel commentReviewTeacherModel) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndReviewTeacherIdAndOwnerEmail(commentId, reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentReviewTeacher == null) return null;
        commentReviewTeacher.setComment(commentReviewTeacherModel.getComment());

        return commentReviewTeacherRepository.save(commentReviewTeacher);
    }

    @Override
    public boolean hiddenCommentReviewTeacher(Long commentId, Long reviewTeacherId) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndReviewTeacherIdAndHiddenAndReviewTeacherOwnerEmail(commentId, reviewTeacherId, false, SecurityContextHolder.getContext().getAuthentication().getName());
        if (commentReviewTeacher == null) return false;
        commentReviewTeacher.setHidden(true);
        return false;
    }

    @Override
    public ReviewTeacher createReviewTeacher(Long teacherId, ReviewTeacherModel reviewTeacherModel) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null)
            return null;
        ReviewTeacher reviewTeacher = new ReviewTeacher();
        reviewTeacher.setTeacher(teacher);
        reviewTeacher.setDone(reviewTeacherModel.getDone() == 1);
        reviewTeacher.setApproved(ApproveType.NEW);
        reviewTeacher.setReview(reviewTeacherModel.getReview());
        reviewTeacher
                .setOwner(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        return reviewTeacherRepository.save(reviewTeacher);
    }

    @Override
    public boolean deleteReviewTeacher(Long reviewTeacherId, Long teacherId) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndTeacherIdAndOwnerEmail(reviewTeacherId, teacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (reviewTeacher == null) return false;
        reviewTeacherRepository.delete(reviewTeacher);
        return true;
    }

    @Override
    public boolean updateReviewTeacher(Long reviewTeacherId, Long teacherId, ReviewTeacherModel reviewTeacherModel) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndTeacherIdAndOwnerEmail(reviewTeacherId, teacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (reviewTeacher == null) return false;
        reviewTeacher.setDone(reviewTeacherModel.getDone() == 1);
        reviewTeacher.setReview(reviewTeacher.getReview());
        reviewTeacherRepository.save(reviewTeacher);
        return true;
    }

    @Override
    public ReviewTeacher approveReviewTeacher(Long reviewTeacherId) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndApproved(reviewTeacherId, ApproveType.NEW);
        if(reviewTeacher == null) {return null;}
        reviewTeacher.setApproved(ApproveType.APPROVED);
        return reviewTeacherRepository.save(reviewTeacher);
    }

    @Override
    public ReviewTeacher rejectReviewTeacher(Long reviewTeacherId) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndApproved(reviewTeacherId, ApproveType.NEW);
        if(reviewTeacher == null) {return null;}
        reviewTeacher.setApproved(ApproveType.REJECT);
        return reviewTeacherRepository.save(reviewTeacher);
    }

    @Override
    public boolean hiddenCommentReviewTeacher(Long commentId) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndIsHidden(commentId, false);
        if(commentReviewTeacher == null) return false;
        commentReviewTeacher.setHidden(true);
        commentReviewTeacherRepository.save(commentReviewTeacher);
        pusherService.triggerChanel("comment-review-teacher-" + commentReviewTeacher.getReviewTeacher().getId(), "hidden-comment",
                commentId);
        return true;
    }

    @Override
    public boolean approveCommentReviewTeacher(Long commentId) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndIsHidden(commentId, false);
        if(commentReviewTeacher == null) return false;
        commentReviewTeacher.setScore(1F);
        commentReviewTeacherRepository.save(commentReviewTeacher);
        return true;
    }

    @Override
    public List<CommentReviewTeacher> getAllBabComments() {
        return commentReviewTeacherRepository.findAllBabComments();
    }

}
