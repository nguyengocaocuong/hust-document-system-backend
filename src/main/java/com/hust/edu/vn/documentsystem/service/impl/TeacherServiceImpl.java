package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.storage.Acl;
import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.common.type.ReportStatus;
import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.CommentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReportContentReviewTeacherModel;
import com.hust.edu.vn.documentsystem.data.model.ReviewTeacherModel;
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

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    private final ReportContentReviewTeacherRepository reportContentReviewTeacherRepository;
    private final FavoriteReviewTeacherRepository favoriteReviewTeacherRepository;
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final TeacherRepository teacherRepository;

    private final ModelMapperUtils modelMapperUtils;
    private final UserRepository userRepository;

    private final GoogleCloudStorageService googleCloudStorageService;
    private final SubjectRepository subjectRepository;

    public TeacherServiceImpl(
            TeacherRepository teacherRepository,
            ModelMapperUtils modelMapperUtils,
            UserRepository userRepository,
            GoogleCloudStorageService googleCloudStorageService,
            SubjectRepository subjectRepository,
            ReviewTeacherRepository reviewTeacherRepository,
            FavoriteReviewTeacherRepository favoriteReviewTeacherRepository,
            ReportContentReviewTeacherRepository reportContentReviewTeacherRepository) {
        this.teacherRepository = teacherRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userRepository = userRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.subjectRepository = subjectRepository;
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.favoriteReviewTeacherRepository = favoriteReviewTeacherRepository;
        this.reportContentReviewTeacherRepository = reportContentReviewTeacherRepository;
    }

    @Override
    public Teacher createTeacher(TeacherModel teacherModel) {
        if (teacherRepository.findByEmailHust(teacherModel.getEmailHust()) != null) return null;
        // TODO: kiểm tra và lưu avatar giáo viên
        Teacher teacher = modelMapperUtils.mapAllProperties(teacherModel, Teacher.class);
        return teacherRepository.save(teacher);
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
        if (reviewTeacher == null) return false;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        FavoriteReviewTeacher favoriteReviewTeacher = favoriteReviewTeacherRepository.findByReviewTeacherAndUser(reviewTeacher, user);
        if (favoriteReviewTeacher != null) {
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
    public List<FavoriteReviewTeacher> getAllFavoriteReviewTeacher(Long reviewTeacherId) {
        return favoriteReviewTeacherRepository.findAllByReviewTeacherId(reviewTeacherId);
    }

    @Override
    public boolean deleteReviewTeacher(Long reviewTeacherId) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findByIdAndUserEmail(reviewTeacherId, SecurityContextHolder.getContext().getAuthentication().getName());
        if (reviewTeacher == null) return false;
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

    @Override
    public boolean updateTeacher(TeacherModel teacherModel) {
        Teacher teacher = teacherRepository.findById(teacherModel.getId()).orElse(null);
        if (teacher == null) return false;
        teacher.setAvatar(teacherModel.getAvatar());
        if (teacherModel.getAvatarFile() != null) {
            try {
                List<String> urls = googleCloudStorageService.createThumbnailAndUploadDocumentToGCP(teacherModel.getAvatarFile(), List.of(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)));
                teacherModel.setAvatar(urls.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        teacher.setName(teacherModel.getName());
        teacher.setDescription(teacherModel.getDescription());
        teacher.setDob(teacherModel.getDob());
        teacher.setEmailHust(teacherModel.getEmailHust());
        teacher.setPhoneNumber(teacherModel.getPhoneNumber());
        teacher.setEmailPrivate(teacherModel.getEmailPrivate());
        teacherRepository.save(teacher);
        return true;
    }

    @Override
    public boolean deleteTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) return false;
        teacherRepository.deleteById(teacherId);
        return true;
    }

    @Override
    public ReviewTeacher createReviewTeacher(Long teacherId, ReviewTeacherModel reviewTeacherModel) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null) return null;
        ReviewTeacher reviewTeacher = new ReviewTeacher();
        reviewTeacher.setTeacher(teacher);
        reviewTeacher.setDone(reviewTeacherModel.getDone() == 1);
        reviewTeacher.setApproved(ApproveType.NEW);
        reviewTeacher.setReview(reviewTeacherModel.getReview());
        reviewTeacher.setOwner(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        return reviewTeacherRepository.save(reviewTeacher);
    }

    @Override
    public ReportContentReviewTeacher createReportContentReviewTeacher(Long reviewTeacherId, ReportContentReviewTeacherModel reportContentReviewTeacherModel) {
        ReviewTeacher reviewTeacher = reviewTeacherRepository.findById(reviewTeacherId).orElse(null);
        if (reviewTeacher == null) return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        ReportContentReviewTeacher reportContentReviewTeacher = new ReportContentReviewTeacher();
        reportContentReviewTeacher.setReviewTeacher(reviewTeacher);
        reportContentReviewTeacher.setOwner(user);
        reportContentReviewTeacher.setMessage(reportContentReviewTeacherModel.getMessage());
        reportContentReviewTeacher.setStatus(ReportStatus.NEW_REPORT);
        return reportContentReviewTeacherRepository.save(reportContentReviewTeacher);
    }
}
