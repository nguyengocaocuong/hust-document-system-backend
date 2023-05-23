package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.event.NotifyEvent;
import com.hust.edu.vn.documentsystem.repository.CommentReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.ReviewTeacherRepository;
import com.hust.edu.vn.documentsystem.repository.TeacherRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.ReviewTeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewTeacherServiceImpl implements ReviewTeacherService {
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    private final ModelMapperUtils modelMapperUtils;
    private final CommentReviewTeacherRepository commentReviewTeacherRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public ReviewTeacherServiceImpl(
            ReviewTeacherRepository reviewTeacherRepository,
            UserRepository userRepository,
            TeacherRepository teacherRepository, ModelMapperUtils modelMapperUtils,
            CommentReviewTeacherRepository commentReviewTeacherRepository, ApplicationEventPublisher publisher) {
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.commentReviewTeacherRepository = commentReviewTeacherRepository;
        this.publisher = publisher;
    }

    @Override
    public List<ReviewTeacher> findReviewByTeacherAndSubject(Long teacherId, Long subjectId) {
        return reviewTeacherRepository.findReviewsByTeacherIdAndSubjectId(teacherId, subjectId);
    }

    @Override
    public List<ReviewTeacher> getAllReviewTeachers() {
        return reviewTeacherRepository.findByDone(true);
    }

    @Override
    public List<ReviewTeacher> getAllReviewTeacherCreatedByUser() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return reviewTeacherRepository.findByOwner(user);
    }

    @Override
    public ReviewTeacher createNewReview(ReviewTeacherModel reviewTeacherModel) {
        Teacher teacher = teacherRepository.findById(reviewTeacherModel.getTeacherId()).orElse(null);
        if (teacher == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewTeacher reviewTeacher = modelMapperUtils.mapAllProperties(reviewTeacherModel, ReviewTeacher.class);
        reviewTeacher.setTeacher(teacher);
        reviewTeacher.setOwner(user);
        reviewTeacher.setDone(reviewTeacherModel.getDone() ==1 );
        reviewTeacher = reviewTeacherRepository.save(reviewTeacher);
        publisher.publishEvent(new NotifyEvent(NotificationType.NEW_REVIEW_TEACHER, reviewTeacher));
        return reviewTeacher;
    }

    @Override
    public ReviewTeacher getReviewTeacherById(Long reviewId) {
        if (reviewId == null) return null;
        return reviewTeacherRepository.findByIdAndDone(reviewId, true);
    }

    @Override
    public boolean updateReview(ReviewTeacherModel reviewTeacherModel) {
        if (reviewTeacherModel.getId() == null)
            return false;
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewTeacherModel.getId()).orElse(null);
        Teacher teacher = teacherRepository.findById(reviewTeacherModel.getTeacherId()).orElse(null);
        if (teacher == null || reviewTeacher == null || !reviewTeacher.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        reviewTeacher.setReview(reviewTeacherModel.getReview());
        reviewTeacher.setDone(reviewTeacherModel.getDone() ==1);
        reviewTeacher.setTeacher(teacher);
        reviewTeacherRepository.save(reviewTeacher);
        publisher.publishEvent(new NotifyEvent(NotificationType.EDIT_REVIEW_TEACHER, reviewTeacher));
        return true;
    }

    @Override
    public boolean deleteReviewTeacherById(Long reviewId) {
        if (reviewId == null)
            return false;
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewId).orElse(null);
        if (reviewTeacher == null || !reviewTeacher.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        reviewTeacherRepository.delete(reviewTeacher);
        publisher.publishEvent(new NotifyEvent(NotificationType.DELETE_REVIEW_TEACHER, reviewTeacher));
        return true;
    }

    @Override
    public boolean updateCommentForCommentReviewTeacher(CommentReviewTeacherModel commentReviewTeacherModel) {
        if (commentReviewTeacherModel.getId() == null) return false;
        CommentReviewTeacher comment = commentReviewTeacherRepository.findById(commentReviewTeacherModel.getId()).orElse(null);
        if (comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        comment.setComment(commentReviewTeacherModel.getComment());
        commentReviewTeacherRepository.save(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.EDIT_COMMENT_REVIEW_TEACHER, comment));
        return true;
    }

    @Override
    public boolean deleteCommentForReviewTeacher(Long id) {
        CommentReviewTeacher comment = commentReviewTeacherRepository.findById(id).orElse(null);
        if (comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        commentReviewTeacherRepository.delete(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.DELETE_COMMENT_REVIEW_TEACHER, comment));
        return true;
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
    public CommentReviewTeacher createCommentForReviewTeacher(CommentReviewTeacherModel commentReviewTeacherModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(commentReviewTeacherModel.getReviewTeacherId()).orElse(null);
        if (reviewTeacher == null || user == null || !reviewTeacher.isDone() || reviewTeacher.isHidden()) return null;
        CommentReviewTeacher commentReviewTeacher = modelMapperUtils.mapAllProperties(commentReviewTeacherModel, CommentReviewTeacher.class);
        commentReviewTeacher.setReviewTeacher(reviewTeacher);
        commentReviewTeacher.setOwner(user);
        commentReviewTeacher = commentReviewTeacherRepository.save(commentReviewTeacher);
        publisher.publishEvent(new NotifyEvent(NotificationType.NEW_COMMENT_REVIEW_TEACHER, commentReviewTeacher));
        return commentReviewTeacher;


    }

    @Override
    public boolean hiddenReviewTeacher(Long id) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndIsHidden(id, false);
        if (reviewTeacher == null || reviewTeacher.isHidden() || userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getRoleType() != RoleType.ADMIN) return false;
        reviewTeacher.setHidden(true);
        reviewTeacherRepository.save(reviewTeacher);
        publisher.publishEvent(new NotifyEvent(NotificationType.HIDDEN_REVIEW_TEACHER, reviewTeacher));
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
    public boolean activeReviewTeacher(Long id) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndIsHidden(id, true);
        if (reviewTeacher == null || !reviewTeacher.isHidden() || user.getRoleType() != RoleType.ADMIN) return false;
        reviewTeacher.setHidden(false);
        reviewTeacherRepository.save(reviewTeacher);
        publisher.publishEvent(new NotifyEvent(NotificationType.ACTIVE_REVIEW_TEACHER, reviewTeacher));
        return true;
    }

    @Override
    public boolean approvedReviewTeacher(Long id) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(id).orElse(null);
        if( reviewTeacher == null || reviewTeacher.getApproved() == ApproveType.APPROVED) return false;
        reviewTeacher.setApproved(ApproveType.APPROVED);
        reviewTeacherRepository.save(reviewTeacher);
        return true;
    }
    @Override
    public boolean unApprovedReviewTeacher(Long id) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(id).orElse(null);
        if( reviewTeacher == null || reviewTeacher.getApproved() == ApproveType.REJECT) return false;
        reviewTeacher.setApproved(ApproveType.REJECT);
        reviewTeacherRepository.save(reviewTeacher);
        return true;
    }
}
