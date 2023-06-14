package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.event.NotifyEvent;
import com.hust.edu.vn.documentsystem.repository.CommentReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.ReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ReviewTeacherServiceImpl implements ReviewTeacherService {
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final CommentReviewTeacherRepository commentReviewTeacherRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public ReviewTeacherServiceImpl(
            ReviewTeacherRepository reviewTeacherRepository,
            CommentReviewTeacherRepository commentReviewTeacherRepository, ApplicationEventPublisher publisher) {
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.commentReviewTeacherRepository = commentReviewTeacherRepository;
        this.publisher = publisher;
    }

    @Override
    public ReviewTeacher getReviewTeacherById(Long reviewId) {
        if (reviewId == null) return null;
        return reviewTeacherRepository.findByIdAndDone(reviewId, true);
    }

    @Override
    public boolean hiddenCommentForReviewTeacher(Long id) {
        CommentReviewTeacher comment = commentReviewTeacherRepository.findById(id).orElse(null);
        if (comment == null || !comment.getReviewTeacher().getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        comment.setHidden(true);
        commentReviewTeacherRepository.save(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.HIDDEN_COMMENT_REVIEW_TEACHER, comment));
        return true;
    }



    @Override
    public boolean activeCommentForReviewTeacher(Long id) {
        CommentReviewTeacher comment = commentReviewTeacherRepository.findById(id).orElse(null);
        if (comment == null || !comment.getReviewTeacher().getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) || !comment.isHidden())
            return false;
        comment.setHidden(false);
        commentReviewTeacherRepository.save(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.ACTIVE_COMMENT_REVIEW_TEACHER, comment));
        return true;
    }

    @Override
    public List<ReviewTeacher> getAllNewReviewTeacher() {
        return reviewTeacherRepository.findAllNewReviewTeacher();
    }

    @Override
    public List<Object[]> getReviewForDashboard(Date sevenDaysAgo) {
        return reviewTeacherRepository.getReviewForDashboard(sevenDaysAgo);
    }


}
