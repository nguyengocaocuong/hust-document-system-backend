package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.CommentReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.ReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.TeacherRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;

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

    
    public ReviewTeacherServiceImpl(
            ReviewTeacherRepository reviewTeacherRepository,
            CommentReviewTeacherRepository commentReviewTeacherRepository,
            UserRepository userRepository,
            TeacherRepository teacherRepository) {
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.commentReviewTeacherRepository = commentReviewTeacherRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
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
        return commentReviewTeacherRepository.findAllByIdAndHidden(reviewTeacherId,false);
    }

    @Override
    public CommentReviewTeacher createCommentForReviewTeacher(Long reviewTeacherId, CommentReviewTeacherModel commentReviewTeacherModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewTeacherId).orElse(null);
        if(reviewTeacher == null) return null;
        CommentReviewTeacher commentReviewTeacher = new CommentReviewTeacher();
        commentReviewTeacher.setReviewTeacher(reviewTeacher);
        commentReviewTeacher.setComment(commentReviewTeacherModel.getComment());
        commentReviewTeacher.setOwner(user);
        if(commentReviewTeacherModel.getParentCommentId() != null){
            CommentReviewTeacher parent = commentReviewTeacherRepository.findById(commentReviewTeacherModel.getParentCommentId()).orElse(null);
            if(parent== null) return null;
            commentReviewTeacher.setParentComment(parent);
        }
        return commentReviewTeacherRepository.save(commentReviewTeacher);
    }

    @Override
    public boolean deleteCommentReviewTeacher(Long commentId, Long reviewTeacherId) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndReviewTeacherIdAndOwnerEmail(commentId,reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(commentReviewTeacher == null) return false;
        commentReviewTeacherRepository.delete(commentReviewTeacher);
        return true;
    }

    @Override
    public boolean updateCommentReviewTeacher(Long commentId, Long reviewTeacherId, CommentReviewTeacherModel commentReviewTeacherModel) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndReviewTeacherIdAndOwnerEmail(commentId,reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(commentReviewTeacher == null) return false;
        commentReviewTeacher.setComment(commentReviewTeacherModel.getComment());
        commentReviewTeacherRepository.save(commentReviewTeacher);
        return true;
    }

    @Override
    public boolean hiddenCommentReviewTeacher(Long commentId, Long reviewTeacherId) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndReviewTeacherIdAndHiddenAndReviewTeacherOwnerEmail(commentId,reviewTeacherId, false, SecurityContextHolder.getContext().getAuthentication().getName());
        if(commentReviewTeacher == null) return false;
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
        if(reviewTeacher == null) return false;
        reviewTeacher.setDone(reviewTeacherModel.getDone() == 1);
        reviewTeacher.setReview(reviewTeacher.getReview());
        reviewTeacherRepository.save(reviewTeacher);
        return true;
    }

}
