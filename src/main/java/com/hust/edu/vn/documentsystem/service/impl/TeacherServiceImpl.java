package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    private final CommentReviewTeacherRepository commentReviewTeacherRepository;
    private final FavoriteReviewTeacherRepository favoriteReviewTeacherRepository;
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final TeacherRepository teacherRepository;

    private final ModelMapperUtils modelMapperUtils;
    private final UserRepository userRepository;

    private final GoogleCloudStorageService googleCloudStorageService;
    private final SubjectRepository subjectRepository;

    @Autowired
    public TeacherServiceImpl(
            TeacherRepository teacherRepository,
            ModelMapperUtils modelMapperUtils,
            UserRepository userRepository,
            GoogleCloudStorageService googleCloudStorageService,
            SubjectRepository subjectRepository,
            ReviewTeacherRepository reviewTeacherRepository,
            FavoriteReviewTeacherRepository favoriteReviewTeacherRepository,
            CommentReviewTeacherRepository commentReviewTeacherRepository) {
        this.teacherRepository = teacherRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userRepository = userRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.subjectRepository = subjectRepository;
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.favoriteReviewTeacherRepository = favoriteReviewTeacherRepository;
        this.commentReviewTeacherRepository = commentReviewTeacherRepository;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    @Override
    public Teacher createTeacher(TeacherModel teacherModel) {
        if (teacherRepository.findByEmailHust(teacherModel.getEmailHust()) != null) return null;
        // TODO: kiểm tra và lưu avatar giáo viên
        Teacher teacher = modelMapperUtils.mapAllProperties(teacherModel, Teacher.class);
        return teacherRepository.save(teacher);
    }






    @Override
    public boolean addSubjects(TeacherModel teacherModel) {
        Teacher teacher = teacherRepository.findById(teacherModel.getId()).orElse(null);
        if (teacher == null)
            return false;
        Subject subject = subjectRepository.findById(teacherModel.getSubjects().get(0)).orElse(null);
        if (subject == null || !teacher.getSubjects().contains(subject)) return false;
        teacher.getSubjects().add(subject);
        teacherRepository.save(teacher);
        return true;
    }

    @Override
    public List<TeacherDto> getAllTeachersForFilter() {
        return teacherRepository.findAllTeacherForFilter();
    }

    @Override
    public List<ReviewTeacher> findAllReviewTeacher() {
        return reviewTeacherRepository.findAllReviewTeacher();
    }

    @Override
    public boolean toggleFavoriteReviewTeacher(Long reviewTeacherId) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewTeacherId).orElse(null);
        if(reviewTeacher == null) return false;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        FavoriteReviewTeacher favoriteReviewTeacher = favoriteReviewTeacherRepository.findByReviewTeacherAndUser(reviewTeacher, user);
        if(favoriteReviewTeacher != null){
            favoriteReviewTeacherRepository.delete(favoriteReviewTeacher);
            return true;
        }
        favoriteReviewTeacher = new FavoriteReviewTeacher();
        favoriteReviewTeacher.setReviewTeacher(reviewTeacher);
        favoriteReviewTeacher.setUser(user);
        favoriteReviewTeacherRepository.save(favoriteReviewTeacher);
        return true;
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
    public List<CommentReviewTeacher> getAllCommentForReviewTeacher(Long reviewTeacherId) {
        return commentReviewTeacherRepository.findAllByIdAndHidden(reviewTeacherId,false);
    }

    @Override
    public boolean deleteCommentReview(Long commentId) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndUserEmail(commentId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(commentReviewTeacher == null) return false;
        commentReviewTeacherRepository.delete(commentReviewTeacher);
        return true;
    }

    @Override
    public boolean updateCommentReview(Long commentId, CommentReviewTeacherModel commentReviewTeacherModel) {
        CommentReviewTeacher commentReviewTeacher = commentReviewTeacherRepository.findByIdAndUserEmail(commentId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(commentReviewTeacher == null) return false;
        commentReviewTeacher.setComment(commentReviewTeacherModel.getComment());
        commentReviewTeacherRepository.save(commentReviewTeacher);
        return true;
    }

    @Override
    public List<FavoriteReviewTeacher> getAllFavoriteReviewTeacher(Long reviewTeacherId) {
        return favoriteReviewTeacherRepository.findAllByReviewTeacherId(reviewTeacherId);
    }

    @Override
    public boolean deleteReviewTeacher(Long reviewTeacherId) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndUserEmail(reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if(reviewTeacher == null) return false;
        reviewTeacherRepository.delete(reviewTeacher);
        return true;
    }

    @Override
    public List<ReviewTeacher> getAllReviewTeacherCreatedByUser() {
        return reviewTeacherRepository.findAllReviewTeacherCreateByUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public List<Object[]> getAllTeacherForAdmin() {
        return teacherRepository.getAllTeacherForAdmin();
    }
}
